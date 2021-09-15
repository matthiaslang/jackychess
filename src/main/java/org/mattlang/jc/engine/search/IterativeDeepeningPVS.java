package org.mattlang.jc.engine.search;

import static java.lang.String.format;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;
import static org.mattlang.jc.engine.sorting.OrderHints.NO_HINTS;

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
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

import lombok.Getter;

public class IterativeDeepeningPVS implements SearchMethod, StatisticsCollector {

    private static final Logger LOGGER = Logger.getLogger(IterativeDeepeningPVS.class.getSimpleName());

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private int maxDepth;

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

        gameContext.initNewMoveSearch(gameState);
        ebf.clear();

        try {
            for (int currdepth = 1; currdepth <= maxDepth; currdepth++) {
                StopWatch roundWatch = new StopWatch();

                UCI.instance.putCommand("info depth " + currdepth);

                roundWatch.start();
                Window aspWindow = new Window(ALPHA_START, BETA_START);

                if (useAspirationWindow && currdepth >= 3) {
                    aspWindow.limitWindow(rslt);
                    rslt = searchWithAspirationWindow(aspWindow, gameState, gameContext, stopTime, orderHints,
                            currdepth);

                } else {
                    rslt = negaMaxAlphaBeta.searchWithScore(gameState, gameContext,
                            currdepth,
                            aspWindow.getAlpha(), aspWindow.getBeta(),
                            stopTime, orderHints);
                }

                if (rslt.savedMove != null) {
                    savedMove = rslt.savedMove;
                }
                if (rslt.savedMove != null) {
                    printRoundInfo(gameContext, rslt, watch, negaMaxAlphaBeta);
                } else {
                    // todo why does this happen that no best move gets returned from nega max search...
                    // we need to further analyze this situation.
                    UCILogger.log("no result from negamax for window: " + aspWindow.descr());
                    LOGGER.info("no result from negamax on iteration depth:" + currdepth);
                    LOGGER.info("negamax result: " + rslt);
                    Map statOfDepth = new LinkedHashMap();
                    negaMaxAlphaBeta.collectStatistics(statOfDepth);

                    LOGGER.info("statistics: " + statOfDepth);
                    if (savedMove != null) {
                        UCILogger.log("best move so far: " + savedMove.toStr());
                    }
                }

                orderHints = new OrderHints(rslt, gameContext, useMvvLvaSorting);

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
            }
        } catch (TimeoutException te) {
            return new IterativeSearchResult(savedMove, rslt);
        } finally {
            //negaMaxAlphaBeta.reset();
        }

        UCILogger.log(format("EBF: %s",ebf.report()));

        return new IterativeSearchResult(savedMove, rslt);
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
        UCI.instance.putCommand("info depth " + rslt.targetDepth +
                " seldepth " + rslt.selDepth +
                " score cp " + rslt.max + " nodes " + nodes
                + " hashfull " + hashfull
                + " nps " + nps + " pv " + rslt.pvList.toPvStr());
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
