package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.JCExecutors;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.util.MoveValidator;

public class AsyncEngine {

    Logger logger = Logger.getLogger("ASYNC");

    /**
     * "inner" future which is asynchronously executed. This future can be cancelled.
     */
    private Future<Move> future;


    private MoveValidator moveValidator = new MoveValidator();

    /**
     * the collected best move so far.
     * We use this to properly return a best move under all circumstances.
     */
    private BestMoveCollector bestMoveCollector;

    /**
     * A Semaphore to guarantee that no two "start" processes are executed simultaneously.
     * We need this, as we do not "wait" on a "stop" command for a process to properly finish because we want
     * no delayed response on "stop".
     * As an asynchronous job may therefore still running/trying to stop when the next "start" is executed
     * we take care by this semaphore to not overlap the calls. Overlaps would be critical, as they would use
     * the same internal structures (movelists, etc) which would result in Exceptions.
     */
    private Semaphore semaphore = new Semaphore(1, true);

    public CompletableFuture<Move> start(final GameState gameState, GameContext gameContext) {
        GoParameter goParams = new GoParameter();
        goParams.infinite = true;
        return start(gameState, goParams, new ConfigValues(), gameContext);
    }

    public CompletableFuture<Move> start(GameState gameState, GoParameter goParams, ConfigValues options,
            GameContext gameContext) {
        // init a first simple best move by ordering via mvalva to have always a best move if
        // we get a stop command before our real search has properly started and returned something better.
        bestMoveCollector = new BestMoveCollector(moveValidator.findSimpleBestMove(gameState));

        // init the search parameters, eval functions, etc:
        SearchParameter searchParams = options.searchAlgorithm.getValue().createSearchParameter();
        searchParams.setConfig(options);
        // if we have special "go" parameters, then override thinktime:
        if (!goParams.infinite) {
            long timeToUse =
                    TimeCalc.determineCalculationTime(gameState, goParams);
            searchParams.getConfig().timeout.setValue((int) timeToUse);

        }
        Factory.setDefaults(searchParams);
        // log parameters only once for a game:
        if (gameContext.getContext("startLogged") == null) {
            Factory.getDefaults().log();
            gameContext.setContext("startLogged", true);
        }

        // start the engine in a separate thread, delivering the result in a future
        CompletableFuture<Move> completableFuture = new CompletableFuture<>();
        Future<Move> newFuture = JCExecutors.EXECUTOR_SERVICE.submit(() -> {
            try {
                // acquire the semaphore, but do not wait endless in case the JVM or Thread gets interrupted
                semaphore.tryAcquire(5, TimeUnit.SECONDS);
                Engine engine = new Engine();
                engine.registerListener(bestMoveCollector);
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
            } finally {
                // release the semaphore. according to the JVM specification in case of thread interruption or
                // JVM exit the "finally" block may not be executed (see https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html);
                // Means we can not ensure that we properly release the semaphore under all circumstance.
                // this is the reason why we use tryAcquire
                // with a timeout; otherwise we got from time to time "hanging" processes in linux and docker
                // when e.g. clients like cutechess gets interrupted by the user.
                semaphore.release();
            }

        });
        this.future = newFuture;
        return completableFuture;
    }

    /**
     * We got a stop command via uci, so we must stop our engine and deliver the best move found so far.
     *
     * @return
     */
    public Move stop() {
        if (future != null) {
            // fire and forget:
            // simply cancel the engine, we even do not wait till it properly stopped
            future.cancel(true);
            future = null;
        }
        // deliver without any delay the best move we got so far:
        return bestMoveCollector.getBestMove();
    }

}
