package org.mattlang.jc.engine.search;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.uci.AsyncEngine;
import org.mattlang.jc.uci.GameContext;

public class MultiThreadedIterativeDeepening implements IterativeDeepeningSearch, StatisticsCollector {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedIterativeDeepening.class.getSimpleName());

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private int maxThreads = Factory.getDefaults().getConfig().maxThreads.getValue();

    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(gameState, gameContext, maxDepth).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(GameState gameState, GameContext gameContext, int maxDepth) {

        if (!gameContext.ttCache.isUsableForLazySmp()) {
            throw new IllegalStateException("Configured TTCache not threadsafe and not usable for Lazy SMP!");
        }

        // start max-1 workerthreads
        List<Future<IterativeSearchResult>> futures = new ArrayList<>();
        for (int i = 1; i < maxThreads; i++) {
            futures.add(startWorker(i, gameState, gameContext, maxDepth));
        }
        // and afterwards start the "main" within this thread as worker 0:
        IterativeDeepeningPVS id = new IterativeDeepeningPVS(0);
        id.registerListener(listener);
        try {
            return id.iterativeSearch(gameState, gameContext, maxDepth);
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

    private Future<IterativeSearchResult> startWorker(int workerNumber, GameState gameState, GameContext gameContext,
            int maxDepth) {
        GameState copiedGame = gameState.copy();
        IterativeDeepeningPVS worker = new IterativeDeepeningPVS(workerNumber);
        Future<IterativeSearchResult> result = AsyncEngine.executorService.submit(() -> {
            try {
                return worker.iterativeSearch(copiedGame, gameContext, maxDepth);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in worker thread!", e);
                throw e;
            }
        });
        return result;
    }

    @Override
    public void collectStatistics(Map stats) {

    }

    @Override
    public void resetStatistics() {

    }
}
