package org.mattlang.jc.engine.search;

import static java.lang.String.format;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;
import static org.mattlang.jc.engine.sorting.OrderHints.NO_HINTS;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS.IterativeRoundResult;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.AsyncEngine;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.MoveValidator;

public class MultiThreadedIterativeDeepeningV2 implements IterativeDeepeningSearch, StatisticsCollector {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedIterativeDeepeningV2.class.getSimpleName());

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta[] = new NegaMaxAlphaBetaPVS[8];

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private boolean useMvvLvaSorting = Factory.getDefaults().getConfig().useMvvLvaSorting.getValue();

    private boolean useAspirationWindow = Factory.getDefaults().getConfig().aspiration.getValue();

    private EffectiveBranchFactor ebf = new EffectiveBranchFactor();

    private MoveValidator moveValidator = new MoveValidator();

    private int maxThreads = Factory.getDefaults().getConfig().maxThreads.getValue();

    public MultiThreadedIterativeDeepeningV2() {
        for (int i = 0; i < negaMaxAlphaBeta.length; i++) {
            negaMaxAlphaBeta[i] = new NegaMaxAlphaBetaPVS();
        }
    }

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(gameState, gameContext, maxDepth).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(GameState gameState, GameContext gameContext, int maxDepth) {
        for (int i = 0; i < maxThreads; i++) {
            negaMaxAlphaBeta[i].reset();
            SearchThreadContexts.CONTEXTS.getContext(i).setOrderHints(NO_HINTS);
        }

        LOGGER.info("iterative search on " + gameState.getFenStr());

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        gameContext.initNewMoveSearch(gameState);
        ebf.clear();
        ArrayList<IterativeRoundResult> rounds = new ArrayList<>();

        IterativeRoundResult lastResults = new IterativeRoundResult(null, NO_HINTS, new StopWatch());
        try {
            for (int currdepth = 1; currdepth <= maxDepth; currdepth++) {

                IterativeRoundResult irr =
                        searchRound(watch, lastResults, gameState, gameContext, currdepth, stopTime);
                lastResults = irr;
                rounds.add(irr);

                if (irr.isCheckMate()) {
                    break;
                }
            }
        } catch (TimeoutException te) {

            String ebfReport = format("EBF: %s", ebf.report());

            UCILogger.log(ebfReport);

            IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
            logIsr(isr);
            return isr;
        } catch (Exception e) {
            throw new SearchException(gameState, gameContext, rounds, ebf.report(), e);
        }

        String ebfReport = format("EBF: %s", ebf.report());

        UCILogger.log(ebfReport);
        LOGGER.info(ebfReport);

        IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
        logIsr(isr);
        return isr;
    }

    private void logIsr(IterativeSearchResult isr) {
        LOGGER.info("best move: " + isr.getSavedMove() + " " + isr.getRslt().toLogString());
    }

    private IterativeRoundResult searchRound(StopWatch watch,
            IterativeRoundResult lastRoundResults,
            GameState gameState, GameContext gameContext, int currdepth,
            long stopTime) {

        StopWatch roundWatch = new StopWatch();

        UCI.instance.putCommand("info depth " + currdepth);

        roundWatch.start();
        Window aspWindow = new Window(ALPHA_START, BETA_START);

        NegaMaxResult rslt = null;

        if (useAspirationWindow && currdepth >= 3) {
            aspWindow.limitWindow(lastRoundResults.getRslt());
            rslt = searchWithAspirationWindow(aspWindow, gameState, gameContext, stopTime,
                    currdepth);

        } else {
            rslt = negaMaxMultiThreaded(gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime);
        }

        if (rslt.savedMove != null) {
            printRoundInfo(gameContext, rslt, watch);
            moveValidator.validate(gameState, rslt);
        } else {
            // todo why does this happen that no best move gets returned from nega max search...
            // we need to further analyze this situation.
            UCILogger.log("no result from negamax for window: " + aspWindow.descr());
            LOGGER.warning("no result from negamax on iteration depth:" + currdepth);
            LOGGER.info("negamax result: " + rslt);
            Map statOfDepth = new LinkedHashMap();
            //            negaMaxAlphaBeta.collectStatistics(statOfDepth);

            LOGGER.info("statistics: " + statOfDepth);
        }

        roundWatch.stop();
        ebf.update(currdepth, roundWatch.getDuration(), rslt.nodesVisited);

        Map statOfDepth = new LinkedHashMap();
        for (int i = 0; i < maxThreads; i++) {
            // todo would not work that way: we do not know if all threads would have run and would have statistics...
            // either remove the statistics stuff or it should be done inside the working thread itself.

//            negaMaxAlphaBeta[i].collectStatistics(statOfDepth);
//            stats.put("depth=" + currdepth, statOfDepth);
//            negaMaxAlphaBeta[i].resetStatistics();
//
//            // experiment: !!!
//            // reset cache after each depth, otherwise we would get cache fails with previous lower depth results
//            // which is not useful
//            negaMaxAlphaBeta[i].resetCaches();
        }

        return new IterativeRoundResult(rslt, null, roundWatch);
    }

    private NegaMaxResult negaMaxMultiThreaded(GameState gameState, GameContext gameContext, int depth,
            int alpha, int beta,
            long stopTime) {

        CompletionService<NegaMaxResult> completionService =
                new ExecutorCompletionService<>(AsyncEngine.executorService);

        // start all workers:
        List<Future<NegaMaxResult>> futures = new ArrayList<>();
        for (int i = 0; i < maxThreads; i++) {
            futures.add(completionService.submit(
                    startNegaMaxWorker(i, gameState, gameContext, depth, alpha, beta, stopTime)));
        }
        NegaMaxResult result = null;

        try {
            // take the first one finished and returning a result:
            for (int i = 0; i < maxThreads; ++i) {
                try {
                    NegaMaxResult r = completionService.take().get();
                    if (r != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ee) {
                    // rethrow timeout exceptions
                    if (ee.getCause() instanceof TimeoutException) {
                        throw ((TimeoutException) ee.getCause());
                    }
                } catch (InterruptedException ignore) {

                }
            }
        } finally {
            for (Future<NegaMaxResult> f : futures) {
                f.cancel(true);
            }

            // wait that all have really stopped before we try a next round...
            for (Future<NegaMaxResult> f : futures) {
                try {
                    f.get();
                } catch (CancellationException | InterruptedException | ExecutionException e) {
                    // catch especially cancellation excptions coming from our cancel before...
                }
            }
        }
        return result;

    }

    private Callable<NegaMaxResult> startNegaMaxWorker(int i, GameState gameState, GameContext gameContext, int depth,
            int alpha, int beta, long stopTime) {

        GameState copiedGame = gameState.copy();
        SearchThreadContext stc = SearchThreadContexts.CONTEXTS.getContext(i);
        OrderHints orderHints = stc.getOrderHints();

        NegaMaxAlphaBetaPVS negaMax = negaMaxAlphaBeta[i];
        return new Callable<NegaMaxResult>() {

            @Override
            public NegaMaxResult call() throws Exception {
                return negaMax.searchWithScore(stc, copiedGame, gameContext,
                        depth,
                        alpha, beta,
                        stopTime, orderHints);
            }
        };
    }

    private NegaMaxResult searchWithAspirationWindow(
            Window aspWindow, GameState gameState, GameContext gameContext,
            long stopTime, int currdepth) {

        LOGGER.fine(format("aspiration start on depth %s %s", currdepth, aspWindow.descr()));

        NegaMaxResult rslt = negaMaxMultiThreaded(gameState, gameContext,
                currdepth,
                aspWindow.getAlpha(), aspWindow.getBeta(),
                stopTime);

        while (aspWindow.outsideWindow(rslt)) {
            aspWindow.widenWindow(rslt);
            LOGGER.fine(format("aspiration widened to %s", aspWindow.descr()));
            rslt = negaMaxMultiThreaded(gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime);
        }
        LOGGER.fine(format("aspiration stabilized: %s", aspWindow.descr()));
        return rslt;
    }

    private void printRoundInfo(
            GameContext gameContext,
            NegaMaxResult rslt,
            StopWatch watch) {
        long nodes = rslt.nodesVisited;
        long duration = watch.getCurrDuration();
        long nps = duration == 0 ? nodes : nodes * 1000 / duration;

        long hashfull = gameContext.ttCache.calcHashFull();
        //        long hashfull = gameContext.ttc.getUsagePercentage();
        UCI.instance.putCommand("info depth " + rslt.targetDepth +
                " seldepth " + rslt.selDepth +
                " score cp " + rslt.max + " nodes " + nodes
                + " hashfull " + hashfull
                + " nps " + nps
                + " time " + duration
                + " pv " + rslt.pvList.toPvStr());
        UCI.instance.putCommand("info currmove " + rslt.savedMove.toStr());
    }

    private Map stats = new LinkedHashMap();

    @Override
    public void collectStatistics(Map stats) {
        stats.put("it.deep", this.stats);
    }

    @Override
    public void resetStatistics() {
        stats = new LinkedHashMap();
    }
}
