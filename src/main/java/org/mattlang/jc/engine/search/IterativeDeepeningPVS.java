package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.BETA_START;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
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
    public Move search(GameState gameState, int depth) {
        negaMaxAlphaBeta.reset();
        this.maxDepth = depth;
        int currdepth = 1;
        Move savedMove = null;

        StopWatch watch = new StopWatch();
        watch.start();

        long stopTime = System.currentTimeMillis() + timeout;

        BoardRepresentation currBoard = gameState.getBoard();
        Color color = gameState.getWho2Move();

        // write a kind of UCI "header" for our stats:
        UCILogger.log(" Depth\t nodes/visited \t quiescence\t alpha beta cutoff\t score");

        PVList pvList = new PVList();
        MoveList moves = negaMaxAlphaBeta.generateMoves(currBoard, color);
        try {
            for (currdepth = 1; currdepth <= maxDepth; currdepth++) {

                UCI.instance.putCommand("info depth " + currdepth);

                NegaMaxResult rslt =
                        negaMaxAlphaBeta.searchWithScore(gameState, currdepth,
                                ALPHA_START, BETA_START, moves,
                                stopTime, pvList);

                savedMove = negaMaxAlphaBeta.getSavedMove();

                if (savedMove != null) {
                    long nodes = negaMaxAlphaBeta.getNodesVisited();
                    long duration = watch.getCurrDuration();
                    long nps = duration == 0? nodes : nodes  * 1000 / duration;
                    UCI.instance.putCommand("info depth " + currdepth + " score cp " + negaMaxAlphaBeta.getSavedMoveScore() + " nodes " + nodes
                            + " nps " + nps + " pv " + rslt.pvList.toPvStr());
                    UCI.instance.putCommand("info currmove " + savedMove.toStr());
                }
                pvList = rslt.pvList;
                moves = reOrderMoves(rslt.moveScores);

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

    public static MoveList reOrderMoves(List<MoveScore> rslt) {
        // order highest scores for us first:
        rslt.sort((o1, o2) -> o2.score - o1.score);
        List<Move> list = rslt.stream().map(s -> s.move).collect(toList());
        return new BasicMoveList(list);
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
