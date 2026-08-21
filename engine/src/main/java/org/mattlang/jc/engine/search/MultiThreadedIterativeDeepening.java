package org.mattlang.jc.engine.search;

import static java.util.Collections.synchronizedList;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.JCExecutors;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.uci.GameContext;

public class MultiThreadedIterativeDeepening implements IterativeDeepeningSearch, StatisticsCollector, IterativeDeepeningListener {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedIterativeDeepening.class.getSimpleName());

    private final int maxThreads = ConfigValues.getConfigValues().maxThreads.getValue();

    private volatile IterativeRoundResult lastIRR = null;


    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(new SearchParameter(SearchParameter.DEFAULT_SEARCHTIME, maxDepth), gameState,
                gameContext).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(SearchParameter searchParams, GameState gameState,
                                                 GameContext gameContext) {

        // start max-1 workerthreads
        List<Future<IterativeSearchResult>> futures = new ArrayList<>();
        for (int i = 1; i < maxThreads; i++) {
            futures.add(startWorker(i, searchParams, gameState, gameContext));
        }
        // and afterward start the "main" within this thread as worker 0:
        IterativeDeepeningPVS id = new IterativeDeepeningPVS(0);
        id.registerListener(this);
        try {
            IterativeSearchResult resultOfFirstThread = id.iterativeSearch(searchParams, gameState, gameContext);
            /*
                create result of the collected results of all search threads:
                we use the ebf report of the first thread, not perfect but this is anyway only used in tests/analysis.
             */
            synchronized (this) {
                if (lastIRR != null) {
                    return new IterativeSearchResult(List.of(lastIRR), resultOfFirstThread.getEbfReport());
                }
            }
            return resultOfFirstThread;
        } finally {
            stopAllWorker(futures);
        }
    }

    @Override
    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = requireNonNull(listener);
    }

    private void stopAllWorker(List<Future<IterativeSearchResult>> futures) {
        for (Future<IterativeSearchResult> future : futures) {
            future.cancel(true);
        }
    }

    private Future<IterativeSearchResult> startWorker(int workerNumber, SearchParameter searchParams,
                                                      GameState gameState, GameContext gameContext) {
        GameState copiedGame = gameState.copy();
        IterativeDeepeningPVS worker = new IterativeDeepeningPVS(workerNumber);
        worker.registerListener(this);
        return JCExecutors.EXECUTOR_SERVICE.submit(() -> {
            try {
                return worker.iterativeSearch(searchParams, copiedGame, gameContext);
            } catch (StopException stopException) {
                // do not log stop exceptions as this is the normal case. just rethrow it:
                throw stopException;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in worker thread!", e);
                throw e;
            }
        });
    }

    @Override
    public void collectStatistics(Map stats) {

    }

    @Override
    public void resetStatistics() {

    }

    @Override
    public synchronized void updateBestRoundMove(NegaMaxResult bestRoundResult) {
        if (lastIRR == null || lastIRR.hasResults() && bestRoundResult.targetDepth >= lastIRR.rslt().targetDepth) {
            listener.updateBestRoundMove(bestRoundResult);
        }
    }


    @Override
    public synchronized void updateIIR(IterativeRoundResult irr) {
        if (lastIRR == null || irr.hasResults() && irr.rslt().targetDepth > lastIRR.rslt().targetDepth) {
            lastIRR = irr;
        }
    }
}
