package org.mattlang.jc.engine.search;

import static java.lang.String.format;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;
import static org.mattlang.jc.engine.sorting.OrderHints.NO_HINTS;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.MoveValidator;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class IterativeDeepeningPVS implements IterativeDeepeningSearch, StatisticsCollector {

    private static final Logger LOGGER = Logger.getLogger(IterativeDeepeningPVS.class.getSimpleName());

    /**
     * Does not bring an improvement: the depth skip makes the performance/results worse... so we dont use it
     */
    // Laser based SMP skip
    //    private static final int[] SMP_SKIP_DEPTHS = { 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 };
    //    private static final int[] SMP_SKIP_AMOUNT = { 1, 2, 1, 2, 3, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6 };
    //    private static final int SMP_MAX_CYCLES = SMP_SKIP_AMOUNT.length;

    /**
     * worker number if this iterative deepening is running inside a worker thread.
     */
    private int workerNumber;

    /**
     * Cycle index, if run as worker thread.
     */
    private int cycleIndex;

    /**
     * true if this is running in a worker thread; false if running as main thread. The main thread
     * is responsible to deliver UCI information and delivers the result.
     */
    private boolean isWorker = false;

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private boolean useMvvLvaSorting = Factory.getDefaults().getConfig().useMvvLvaSorting.getValue();

    private boolean useAspirationWindow = Factory.getDefaults().getConfig().aspiration.getValue();

    private EffectiveBranchFactor ebf = new EffectiveBranchFactor();

    private MoveValidator moveValidator = new MoveValidator();

    public IterativeDeepeningPVS(int workerNumber) {
        this.workerNumber = workerNumber;
        //        cycleIndex = (workerNumber - 1) % SMP_MAX_CYCLES;
        isWorker = workerNumber > 0;
    }

    public IterativeDeepeningPVS(NegaMaxAlphaBetaPVS negaMaxAlphaBeta) {
        this.negaMaxAlphaBeta = negaMaxAlphaBeta;
    }

    public IterativeDeepeningPVS() {
    }

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(gameState, gameContext, maxDepth).getSavedMove();
    }

    @Override
    public IterativeSearchResult iterativeSearch(GameState gameState, GameContext gameContext, int maxDepth) {
        negaMaxAlphaBeta.reset();
        SearchThreadContext stc = SearchThreadContexts.CONTEXTS.getContext(workerNumber);

        LOGGER.info("iterative search on " + gameState.getFenStr());

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        gameContext.initNewMoveSearch(gameState);
        ebf.clear();
        ArrayList<IterativeRoundResult> rounds = new ArrayList<>();

        int startDepth = workerNumber == 0 ? 1 : 2;
        int maxEffDepth = workerNumber > 0 ? maxDepth + 1 : maxDepth;

        IterativeRoundResult lastResults = new IterativeRoundResult(null, NO_HINTS, new StopWatch());
        try {
            int currdepth = startDepth;

            while (currdepth <= maxDepth) {
                currdepth++;
                if (isWorker) {
                    currdepth = adjustDepthForWorker(currdepth);
                    if (currdepth > maxDepth) {
                        break;
                    }
                }

                IterativeRoundResult irr =
                        searchRound(stc, watch, lastResults, gameState, gameContext, currdepth, stopTime);
                lastResults = irr;
                rounds.add(irr);

                if (irr.isCheckMate()) {
                    break;
                }
            }
        } catch (TimeoutException te) {

            String ebfReport = format("EBF: %s", ebf.report());
            if (!isWorker) {
                UCILogger.log(ebfReport);
            }
            IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
            logIsr(isr);
            return isr;
        } catch (Exception e) {
            throw new SearchException(gameState, gameContext, rounds, ebf.report(), e);
        }

        String ebfReport = format("EBF: %s", ebf.report());
        if (!isWorker) {
            UCILogger.log(ebfReport);
            LOGGER.info(ebfReport);
        }

        IterativeSearchResult isr = new IterativeSearchResult(rounds, ebfReport);
        logIsr(isr);
        return isr;
    }

    /**
     * adjusts the depth for workers. We dont do this for now, as it does not give any improvement,
     * indeed it makes the performance and elo worse.
     *
     * @param currDepth
     * @return
     */
    private int adjustDepthForWorker(int currDepth) {
        //        if ((currDepth + cycleIndex) % SMP_SKIP_DEPTHS[cycleIndex] == 0) {
        //            currDepth += SMP_SKIP_AMOUNT[cycleIndex];
        //        }
        return currDepth;
    }

    private void logIsr(IterativeSearchResult isr) {
        if (!isWorker) {
            String bestMove = isr.getSavedMove() != null ? isr.getSavedMove().toStr() : "none";
            String rslt = isr.getRslt() != null ? isr.getRslt().toLogString() : "no results";
            LOGGER.info("best move: " + bestMove + " " + rslt);
        }
    }

    @AllArgsConstructor
    @Getter
    static class IterativeRoundResult {

        private final NegaMaxResult rslt;
        private final OrderHints orderHints;
        private final StopWatch roundWatch;

        public boolean isCheckMate() {
            return Math.abs(Math.abs(rslt.directScore) - Weights.KING_WEIGHT) < 100;
        }

        public boolean hasResults() {
            return rslt != null;
        }
    }

    private IterativeRoundResult searchRound(SearchThreadContext stc, StopWatch watch,
            IterativeRoundResult lastRoundResults,
            GameState gameState, GameContext gameContext, int currdepth,
            long stopTime) {

        StopWatch roundWatch = new StopWatch();

        if (!isWorker) {
            UCI.instance.putCommand("info depth " + currdepth);
        }
        roundWatch.start();
        Window aspWindow = new Window(ALPHA_START, BETA_START);

        NegaMaxResult rslt = null;

        if (useAspirationWindow && currdepth >= 3 && lastRoundResults.hasResults()) {
            aspWindow.limitWindow(lastRoundResults.getRslt());
            rslt = searchWithAspirationWindow(stc, aspWindow, gameState, gameContext, stopTime,
                    lastRoundResults.getOrderHints(),
                    currdepth);

        } else {
            rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime, lastRoundResults.getOrderHints());
        }

        if (rslt.savedMove != null) {
            printRoundInfo(gameContext, rslt, watch, negaMaxAlphaBeta);
            moveValidator.validate(gameState, rslt);
        } else {
            // todo why does this happen that no best move gets returned from nega max search...
            // we need to further analyze this situation.
            UCILogger.log("no result from negamax for window: " + aspWindow.descr());
            LOGGER.warning("no result from negamax on iteration depth:" + currdepth);
            LOGGER.info("negamax result: " + rslt);
            Map statOfDepth = new LinkedHashMap();
            negaMaxAlphaBeta.collectStatistics(statOfDepth);

            LOGGER.info("statistics: " + statOfDepth);
        }

        roundWatch.stop();
        ebf.update(currdepth, roundWatch.getDuration(), negaMaxAlphaBeta.getNodesVisited());

        Map statOfDepth = new LinkedHashMap();
        negaMaxAlphaBeta.collectStatistics(statOfDepth);
        stats.put("depth=" + currdepth, statOfDepth);
        negaMaxAlphaBeta.resetStatistics();

        OrderHints orderHints = new OrderHints(rslt, stc, useMvvLvaSorting);
        return new IterativeRoundResult(rslt, orderHints, roundWatch);
    }

    private NegaMaxResult searchWithAspirationWindow(SearchThreadContext stc,
            Window aspWindow, GameState gameState, GameContext gameContext,
            long stopTime, OrderHints orderHints, int currdepth) {

        LOGGER.fine(format("aspiration start on depth %s %s", currdepth, aspWindow.descr()));

        NegaMaxResult rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                currdepth,
                aspWindow.getAlpha(), aspWindow.getBeta(),
                stopTime, orderHints);

        while (aspWindow.outsideWindow(rslt)) {
            aspWindow.widenWindow(rslt);
            LOGGER.fine(format("aspiration widened to %s", aspWindow.descr()));
            rslt = negaMaxAlphaBeta.searchWithScore(stc, gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime, orderHints);
        }
        LOGGER.fine(format("aspiration stabilized: %s", aspWindow.descr()));
        return rslt;
    }

    private void printRoundInfo(
            GameContext gameContext,
            NegaMaxResult rslt,
            StopWatch watch,
            AlphaBetaSearchMethod negaMaxAlphaBeta) {
        if (!isWorker) {
            long nodes = negaMaxAlphaBeta.getNodesVisited();
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
