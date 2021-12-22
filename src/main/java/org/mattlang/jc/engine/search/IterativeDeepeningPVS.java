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

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private boolean useMvvLvaSorting = Factory.getDefaults().getConfig().useMvvLvaSorting.getValue();

    private boolean useAspirationWindow = Factory.getDefaults().getConfig().aspiration.getValue();

    private EffectiveBranchFactor ebf = new EffectiveBranchFactor();

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

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        gameContext.initNewMoveSearch(gameState);
        ebf.clear();
        ArrayList<IterativeRoundResult> rounds = new ArrayList<>();

        IterativeRoundResult lastResults = new IterativeRoundResult(null, NO_HINTS, new StopWatch());
        try {
            for (int currdepth = 1; currdepth <= maxDepth; currdepth++) {
                IterativeRoundResult irr = searchRound(watch, lastResults, gameState, gameContext, currdepth, stopTime);
                lastResults = irr;
                rounds.add(irr);

                if (irr.isCheckMate()) {
                    break;
                }
            }
        } catch (TimeoutException te) {
            String ebfReport = format("EBF: %s", ebf.report());
            UCILogger.log(ebfReport);
            LOGGER.info(ebfReport);
            return new IterativeSearchResult(rounds, ebfReport);
        } finally {
            //negaMaxAlphaBeta.reset();
        }

        String ebfReport = format("EBF: %s", ebf.report());
        UCILogger.log(ebfReport);
        LOGGER.info(ebfReport);

        return new IterativeSearchResult(rounds, ebfReport);
    }

    @AllArgsConstructor
    @Getter
    static class IterativeRoundResult {

        private final NegaMaxResult rslt;
        private final OrderHints orderHints;
        private final StopWatch roundWatch;

        public boolean isCheckMate() {
            return Math.abs(Math.abs(rslt.directScore)- Weights.KING_WEIGHT)<100;
        }
    }

    private IterativeRoundResult searchRound(StopWatch watch, IterativeRoundResult lastRoundResults,
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
                    lastRoundResults.getOrderHints(),
                    currdepth);

        } else {
            rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime, lastRoundResults.getOrderHints());
        }

        if (rslt.savedMove != null) {
            printRoundInfo(gameContext, rslt, watch, negaMaxAlphaBeta);
            MoveValidator.validate(gameState, rslt);
        } else {
            // todo why does this happen that no best move gets returned from nega max search...
            // we need to further analyze this situation.
            UCILogger.log("no result from negamax for window: " + aspWindow.descr());
            LOGGER.info("no result from negamax on iteration depth:" + currdepth);
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

        // experiment: !!!
        // reset cache after each depth, otherwise we would get cache fails with previous lower depth results
        // which is not useful
        negaMaxAlphaBeta.resetCaches();

        OrderHints orderHints = new OrderHints(rslt, gameContext, useMvvLvaSorting);
        return new IterativeRoundResult(rslt, orderHints, roundWatch);
    }


    private NegaMaxResult searchWithAspirationWindow(Window aspWindow, GameState gameState, GameContext gameContext,
            long stopTime, OrderHints orderHints, int currdepth) {

        LOGGER.info(format("aspiration start on depth %s %s", currdepth, aspWindow.descr()));

        NegaMaxResult rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                currdepth,
                aspWindow.getAlpha(), aspWindow.getBeta(),
                stopTime, orderHints);

        while (aspWindow.outsideWindow(rslt)) {
            aspWindow.widenWindow(rslt);
            LOGGER.info(format("aspiration widened to %s", aspWindow.descr()));
            rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime, orderHints);
        }
        LOGGER.info(format("aspiration stabilized: %s", aspWindow.descr()));
        return rslt;
    }

    public static void printRoundInfo(
            GameContext gameContext,
            NegaMaxResult rslt,
            StopWatch watch,
            AlphaBetaSearchMethod negaMaxAlphaBeta) {
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
