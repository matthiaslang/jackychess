package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;
import static org.mattlang.jc.engine.sorting.OrderHints.NO_HINTS;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

import lombok.Getter;

public class IterativeDeepeningPVS implements SearchMethod, StatisticsCollector {

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private int maxDepth;

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    private boolean useMvvLvaSorting = Factory.getDefaults().getConfig().useMvvLvaSorting.getValue();

    private boolean useAspirationWindow = Factory.getDefaults().getConfig().aspiration.getValue();

    public IterativeDeepeningPVS(NegaMaxAlphaBetaPVS negaMaxAlphaBeta) {
        this.negaMaxAlphaBeta = negaMaxAlphaBeta;
    }

    public IterativeDeepeningPVS() {
    }

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        return iterativeSearch(gameState, gameContext, maxDepth).getSavedMove();
    }

    @Getter
    static class Window {

        int alpha;
        int beta;

        /**
         * the last score where the window is build around.
         */
        int lastScore;

        /**
         * Widening phase: 0 means not widened, just limited.
         * Each increment means another widening of the window happened.
         */
        int wideningPhase = 0;

        public Window(int alpha, int beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        public void limitWindow(NegaMaxResult rslt) {
            lastScore = rslt.max;
            alpha = lastScore - 50;
            beta = lastScore + 50;

            wideningPhase = 0;
        }

        public boolean outsideWindow(NegaMaxResult rslt) {
            int score = rslt.max;
            return score <= alpha || score >= beta;
        }

        public void widenWindow(NegaMaxResult rslt) {
            int delta = wideningPhase++ * 50 + 50;

            int score = rslt.max;
            lastScore = score;
            if (score <= alpha) {
                alpha =alpha -  delta - (alpha - score);
            } else if (score >= beta) {
                beta = beta + delta + (score - beta);
            }

            // after 3 phases, set the full window:

            // todo test: after first limit set it to full window... may be this is better...
            if (wideningPhase > 0) {
                alpha = ALPHA_START;
                beta = BETA_START;
            }
        }

        public String descr() {
            return "lastscore: " + lastScore + " window[" + alpha + ", " + beta + "]";
        }
    }

    public IterativeSearchResult iterativeSearch(GameState gameState, GameContext gameContext, int maxDepth) {
        negaMaxAlphaBeta.reset();
        this.maxDepth = maxDepth;
        Move savedMove = null;
        NegaMaxResult rslt = null;

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        OrderHints orderHints = NO_HINTS;

        try {
            for (int currdepth = 1; currdepth <= maxDepth; currdepth++) {

                UCI.instance.putCommand("info depth " + currdepth);

                Window aspWindow = new Window(ALPHA_START, BETA_START);

                if (useAspirationWindow && currdepth >= 2) {
                    aspWindow.limitWindow(rslt);
                    rslt = searchWithAspirationWindow(aspWindow, gameState, gameContext, stopTime, orderHints,
                            currdepth);

                } else {
                    rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                            currdepth,
                            aspWindow.getAlpha(), aspWindow.getBeta(),
                            stopTime, orderHints);
                }

                savedMove = negaMaxAlphaBeta.getSavedMove();

                if (savedMove != null) {
                    printRoundInfo(rslt, watch, negaMaxAlphaBeta);
                }

                orderHints = new OrderHints(rslt, gameContext, useMvvLvaSorting);

                Map statOfDepth = new LinkedHashMap();
                negaMaxAlphaBeta.collectStatistics(statOfDepth);
                stats.put("depth=" + currdepth, statOfDepth);
                negaMaxAlphaBeta.resetStatistics();

                // experiment: !!!
                // reset cache after each depth, otherwise we would get cache fails with previous lower depth results
                // which is not useful
                negaMaxAlphaBeta.resetCaches();
            }
        } catch (TimeoutException te) {
            return new IterativeSearchResult(savedMove, rslt);
        } finally {
            //negaMaxAlphaBeta.reset();
        }

        return new IterativeSearchResult(savedMove, rslt);
    }

    private NegaMaxResult searchWithAspirationWindow(Window aspWindow, GameState gameState, GameContext gameContext,
            long stopTime, OrderHints orderHints, int currdepth) {

        UCILogger.log("aspiration start on depth %s %s", currdepth, aspWindow.descr());

        NegaMaxResult rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                currdepth,
                aspWindow.getAlpha(), aspWindow.getBeta(),
                stopTime, orderHints);

        while (aspWindow.outsideWindow(rslt)) {
            aspWindow.widenWindow(rslt);
            UCILogger.log("aspiration widened to %s", aspWindow.descr());
            rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                    currdepth,
                    aspWindow.getAlpha(), aspWindow.getBeta(),
                    stopTime, orderHints);
        }
        UCILogger.log("aspiration stabilized: %s", aspWindow.descr());
        return rslt;
    }

    public static void printRoundInfo(
            NegaMaxResult rslt,
            StopWatch watch,
            AlphaBetaSearchMethod negaMaxAlphaBeta) {
        long nodes = negaMaxAlphaBeta.getNodesVisited();
        long duration = watch.getCurrDuration();
        long nps = duration == 0 ? nodes : nodes * 1000 / duration;
        UCI.instance.putCommand("info depth " + rslt.targetDepth +
                " seldepth " + rslt.selDepth +
                " score cp " + negaMaxAlphaBeta.getSavedMoveScore() + " nodes " + nodes
                + " nps " + nps + " pv " + rslt.pvList.toPvStr());
        UCI.instance.putCommand("info currmove " + negaMaxAlphaBeta.getSavedMove().toStr());
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
