package org.mattlang.jc.uci;

import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.TaperedEval;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

import static org.mattlang.jc.uci.UciProcessor.*;


public class AsyncEngine {

    /**
     * "outer" completable future. CompletableFutures unfortunately dont support cancel of asynchronous
     * attached work. therefore we need to work with two futures.
     */
    private CompletableFuture<Move> result;
    /**
     * "inner" future which is asynchronously executed. This future can be cancelled.
     */
    private Future<Move> future;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public CompletableFuture<Move> start(final GameState gameState) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new HashMap<>());
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, Map<String, Long> options) {
        SearchParameter searchParams = Factory.createDefaultParameter();
        if (options.containsKey(OP_MAXDEPTH)) {
            searchParams.setMaxDepth(options.get(OP_MAXDEPTH).intValue());
        }
        if (options.containsKey(OP_THINKTIME)) {
            searchParams.setTimeout(options.get(OP_THINKTIME) * 1000);
        }
        if (options.containsKey(OP_QUIESCENCE)) {
            searchParams.setMaxQuiescenceDepth(options.get(OP_QUIESCENCE).intValue());
        }

        // if we have special "go" parameters, then override thinktime:
        if (!goParams.infinite) {
            long restTime;
            long restMoves;
            if (gameState.getWho2Move() == Color.WHITE) {
                restTime = goParams.wtime;
            } else {
                restTime = goParams.btime;
            }
            restMoves = goParams.movestogo;
            if (restMoves != 0 && restTime > 0) {
                long averageTimeForThisMove = restTime / restMoves;
                searchParams.setTimeout(averageTimeForThisMove);
            }
            if (goParams.movetime > 0) {
                searchParams.setTimeout(goParams.movetime);
            }

        }
        Factory.setDefaults(searchParams);
        Factory.getDefaults().log();
        // log game phase
        TaperedEval taperedEval = new TaperedEval();
        int phase = taperedEval.calcPhase(gameState.getBoard());
        UCILogger.log("Tapered Phase: " + phase);


        CompletableFuture<Move> completableFuture = new CompletableFuture<>();
        Future<Move> future = executorService.submit(() -> {
            Move move = new Engine().go(gameState);
            completableFuture.complete(move);
            return move;
        });

        this.future = future;
        result = completableFuture;
        return result;
    }

    public Optional<Move> stop() {
        if (future != null) {
            future.cancel(true);
            try {
                return Optional.of(result.get(2000, TimeUnit.MILLISECONDS));
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
