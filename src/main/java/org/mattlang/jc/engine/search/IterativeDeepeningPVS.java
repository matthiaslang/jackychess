package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.*;
import static org.mattlang.jc.engine.sorting.OrderHints.NO_HINTS;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class IterativeDeepeningPVS implements SearchMethod, StatisticsCollector {


    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private int maxDepth;

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    public IterativeDeepeningPVS(NegaMaxAlphaBetaPVS negaMaxAlphaBeta) {
        this.negaMaxAlphaBeta = negaMaxAlphaBeta;
    }

    public IterativeDeepeningPVS() {
    }

    @Override
    public Move search(GameState gameState, GameContext gameContext, int maxDepth) {
        negaMaxAlphaBeta.reset();
        this.maxDepth = maxDepth;
        Move savedMove = null;

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        OrderHints orderHints = NO_HINTS;

        try {
            for (int currdepth = 1; currdepth <= maxDepth; currdepth++) {

                UCI.instance.putCommand("info depth " + currdepth);

                NegaMaxResult rslt =
                        negaMaxAlphaBeta.searchWithScore(gameState, currdepth,
                                ALPHA_START, BETA_START,
                                stopTime, orderHints);

                savedMove = negaMaxAlphaBeta.getSavedMove();

                if (savedMove != null) {
                    printRoundInfo(rslt, watch, negaMaxAlphaBeta);
                }

                orderHints = new OrderHints(rslt.pvList, rslt.moveScores, gameContext.getContext(HISTORY_HEURISTIC));

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
            return savedMove;
        } finally {
            //negaMaxAlphaBeta.reset();
        }

        return savedMove;
    }

    public static void printRoundInfo(
            NegaMaxResult rslt,
            StopWatch watch,
            AlphaBetaSearchMethod negaMaxAlphaBeta )
    {
        long nodes = negaMaxAlphaBeta.getNodesVisited();
        long duration = watch.getCurrDuration();
        long nps = duration == 0? nodes : nodes  * 1000 / duration;
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
