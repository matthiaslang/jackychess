package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;

public class AsyncEngine {

    Logger logger = Logger.getLogger("ASYNC");
    /**
     * "outer" completable future. CompletableFutures unfortunately dont support cancel of asynchronous
     * attached work. therefore we need to work with two futures.
     */
    private CompletableFuture<Move> result;
    /**
     * "inner" future which is asynchronously executed. This future can be cancelled.
     */
    private Future<Move> future;

    public static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public CompletableFuture<Move> start(final GameState gameState, GameContext gameContext) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new ConfigValues(), gameContext);
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, ConfigValues options, GameContext gameContext) {
        SearchParameter searchParams = options.searchAlgorithm.getValue().createSearchParameter();
        searchParams.evaluateFunction.set(options.evluateFunctions.getValue().createSupplier());
        searchParams.moveList.set(options.moveListImpls.getValue().createSupplier());
        searchParams.setConfig(options);

        // if we have special "go" parameters, then override thinktime:
        if (!goParams.infinite) {
            if (goParams.movetime > 0) {
                searchParams.getConfig().timeout.setValue((int) goParams.movetime);
            } else {

                long restTime;
                long incTime;
                long opponentRestTime;
                long opponentIncTime;
                long restMoves;
                if (gameState.getWho2Move() == Color.WHITE) {
                    restTime = goParams.wtime;
                    incTime = goParams.winc;
                    opponentRestTime = goParams.btime;
                    opponentIncTime = goParams.binc;
                } else {
                    restTime = goParams.btime;
                    incTime = goParams.binc;
                    opponentRestTime = goParams.wtime;
                    opponentIncTime = goParams.winc;
                }
                restMoves = goParams.movestogo;
                long timeToUse =
                        determineTime(gameState, restTime, opponentRestTime, incTime, opponentIncTime, restMoves);
                searchParams.getConfig().timeout.setValue((int) timeToUse);
            }
        }
        Factory.setDefaults(searchParams);
        Factory.getDefaults().log();

        CompletableFuture<Move> completableFuture = new CompletableFuture<>();
        Future<Move> future = executorService.submit(() -> {
            try {
                Move move = new Engine().go(gameState, gameContext);
                completableFuture.complete(move);
                return move;
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
                logger.log(SEVERE, "error during async execution!", e);
                throw e;
            }

        });

        this.future = future;
        result = completableFuture;
        return result;
    }

    private long determineTime(GameState gameState, long restTime, long opponentRestTime, long incTime,
            long opponentIncTime,
            long restMoves) {
        
        long time = incTime / 2;

        long diff = restTime - opponentRestTime;
        if (diff > 0) {
            time += diff / 3;
        } else {
            time -= diff / 3;
        }

        if (restMoves != 0 && restTime > 0) {
            long averageTimeForThisMove = restTime / restMoves;

            time += averageTimeForThisMove;
        } else {
            // determine by gamephase
            double phase = PhaseCalculator.calcPhaseFactor(gameState.getBoard());
            boolean isOpening = phase > 0.6;
            if (isOpening) {
                // estimate moves by phase:
                // lets say a game has 60 moves in average:
                int averageMovesPerGame = 60;
                // weight by phase where we are
                averageMovesPerGame = (int) (averageMovesPerGame * phase + 5);

                long averageTime = restTime / averageMovesPerGame;

                time += averageTime;
            } else {
                // average end game moves..
                int averageMovesEndGame = 20;
                long averageTime = restTime / averageMovesEndGame;
                time += averageTime;
            }
        }

        // subtract a bit to not overceed the time by engine stop delays:
        if (time > 1000) {
            time -= 200;
        }

        // fallback:
        if (time <= 0) {
            time = 1000;
        }

        // double check chosen time:
        int approxamatelyFutureMoves = (int) (restTime / time);
        if (approxamatelyFutureMoves < 5) {
            time = restTime / 10;
        } else if (approxamatelyFutureMoves < 10) {
            time /= 2;
        }

        if (time > restTime) {
            time = restTime;
            if (time > 800) {
                time -= 200;
            }
        }
        return time;
    }

    public Optional<Move> stop() {
        if (future != null) {
            future.cancel(true);
            try {
                return Optional.ofNullable(result.get(2000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException | IllegalMonitorStateException | ExecutionException | TimeoutException e) {
                //e.printStackTrace();
            }
            result = null;
            future = null;
        } else {
            // stop without go...?

        }
        return Optional.empty();
    }

}
