package org.mattlang.jc.engine.search;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
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

public class MultiThreadedIterativeDeepening implements IterativeDeepeningSearch, StatisticsCollector {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedIterativeDeepening.class.getSimpleName());

    private int maxThreads = ConfigValues.getConfigValues().maxThreads.getValue();

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
        // and afterwards start the "main" within this thread as worker 0:
        IterativeDeepeningPVS id = new IterativeDeepeningPVS(0);
        id.registerListener(listener);
        try {
            return id.iterativeSearch(searchParams, gameState, gameContext);
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
}
