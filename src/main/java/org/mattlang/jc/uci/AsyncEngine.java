package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.Constants.MAX_THREADS;
import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.engine.search.SearchThreadContexts;

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

    public static ExecutorService executorService = Executors.newFixedThreadPool(2 * MAX_THREADS);

    public CompletableFuture<Move> start(final GameState gameState, GameContext gameContext) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new ConfigValues(), gameContext);
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, ConfigValues options, GameContext gameContext) {
        SearchParameter searchParams = options.searchAlgorithm.getValue().createSearchParameter();
        searchParams.evaluateFunction.set(options.evluateFunctions.getValue().getSupplier());
        searchParams.moveList.set(options.moveListImpls.getValue().createSupplier());
        searchParams.setConfig(options);
        // if we have special "go" parameters, then override thinktime:
        if (!goParams.infinite) {
            long timeToUse =
                    TimeCalc.determineCalculationTime(gameState, goParams);
                searchParams.getConfig().timeout.setValue((int) timeToUse);

        }
        Factory.setDefaults(searchParams);
        SearchThreadContexts.CONTEXTS.resetMoveLists();
        // log parameters only once for a game:
        if (gameContext.getContext("startLogged")==null) {
            Factory.getDefaults().log();
            gameContext.setContext("startLogged", true);
        }

        CompletableFuture<Move> completableFuture = new CompletableFuture<>();
        Future<Move> future = executorService.submit(() -> {
            try {
                Move move = new Engine().go(gameState, gameContext);
                completableFuture.complete(move);
                return move;
            } catch (SearchException se){
                completableFuture.completeExceptionally(se);
                logger.log(SEVERE, se.toStringAllInfos(), se);
                throw se;
            } catch (Throwable e) {
                completableFuture.completeExceptionally(e);
                logger.log(SEVERE, fmtSevere(gameState,"error during async execution!"), e);
                throw e;
            }

        });

        this.future = future;
        result = completableFuture;
        return result;
    }

    public Optional<Move> stop() {
        if (future != null) {
            future.cancel(true);
            try {
                return Optional.ofNullable(result.get(2000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException | IllegalMonitorStateException | ExecutionException | TimeoutException e) {
                logger.log(Level.WARNING, "Exception getting result", e);
            }
            result = null;
            future = null;
        } else {
            // stop without go...?
            logger.warning("got 'stop' without having a 'go' or internal error!");

        }
        return Optional.empty();
    }

}
