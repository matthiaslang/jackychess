package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.Constants.MAX_THREADS;
import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.util.MoveValidator;

public class AsyncEngine {

    Logger logger = Logger.getLogger("ASYNC");

    /**
     * "inner" future which is asynchronously executed. This future can be cancelled.
     */
    private Future<Move> future;

    public static ExecutorService executorService = Executors.newFixedThreadPool(2 * MAX_THREADS);

    private MoveValidator moveValidator = new MoveValidator();

    /**
     * the collected best move so far.
     * We use this to properly return a best move under all circumstances.
     */
    private Move bestMove;

    private Engine engine;

    public CompletableFuture<Move> start(final GameState gameState, GameContext gameContext) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new ConfigValues(), gameContext);
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, ConfigValues options,
            GameContext gameContext) {
        // init naively a best move by ordering via mvalva to have always a best move if
        // we get a stop command before our real search has properly started and returned something better.
        bestMove = moveValidator.findSimpleBestMove(gameState);

        // init the search parameters, eval functions, etc:
        SearchParameter searchParams = options.searchAlgorithm.getValue().createSearchParameter();
        searchParams.evaluateFunction.set(options.evluateFunctions.getValue().getSupplier());
        searchParams.moveiterationPreparer.set(options.moveIterationImpls.getValue().createSupplier());
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
        if (gameContext.getContext("startLogged") == null) {
            Factory.getDefaults().log();
            gameContext.setContext("startLogged", true);
        }

        // start the engine in a separate thread, delivering the result in a future
        CompletableFuture<Move> completableFuture = new CompletableFuture<>();
        this.future = executorService.submit(() -> {
            try {
                engine = new Engine();
                Move move = engine.go(gameState, gameContext);
                completableFuture.complete(move);
                return move;
            } catch (SearchException se) {
                completableFuture.completeExceptionally(se);
                logger.log(SEVERE, se.toStringAllInfos(), se);
                throw se;
            } catch (Throwable e) {
                completableFuture.completeExceptionally(e);
                logger.log(SEVERE, fmtSevere(gameState, "error during async execution!"), e);
                throw e;
            }

        });

        return completableFuture;
    }

    /**
     * We got a stop command via uci, so we must stop our engine and deliver the best move found so far.
     *
     * @return
     */
    public Move stop() {
        if (future != null) {
            // simply cancel the engine, we even do not wait till it properly stopped
            future.cancel(true);
            future = null;
        }

        // return the best collected move we have so far
        if (engine != null) {
            return engine.getBestMoveSoFar();
        } else {
            return bestMove;
        }
    }

}
