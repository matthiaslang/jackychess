package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.AlphaBetaSearchMethod;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.uci.UCI;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.BETA_START;

public class IterativeDeepeningNegaMaxAlphaBeta implements SearchMethod {


    private AlphaBetaSearchMethod negaMaxAlphaBeta = new NegaMaxAlphaBeta();

    private int maxDepth;

    private long timeout = Factory.getDefaults().getTimeout();

    public IterativeDeepeningNegaMaxAlphaBeta(AlphaBetaSearchMethod negaMaxAlphaBeta) {
        this.negaMaxAlphaBeta = negaMaxAlphaBeta;
    }

    public IterativeDeepeningNegaMaxAlphaBeta() {
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

        MoveList moves = negaMaxAlphaBeta.generateMoves(currBoard, color);
        try {
            for (currdepth = 1; currdepth <= maxDepth; currdepth++) {

                UCI.instance.putCommand("info depth " + currdepth);

                NegaMaxResult rslt =
                        negaMaxAlphaBeta.searchWithScore(gameState, currdepth,
                                ALPHA_START, BETA_START, moves,
                                stopTime);

                savedMove = negaMaxAlphaBeta.getSavedMove();

                if (savedMove != null) {
                    int nodes = negaMaxAlphaBeta.getNodesVisited();
                    long duration = watch.getCurrDuration();
                    long nps = duration == 0? nodes : nodes  * 1000 / duration;
                    UCI.instance.putCommand("info depth " + currdepth + " score cp " + negaMaxAlphaBeta.getSavedMoveScore() + " nodes " + nodes + " nps " + nps);
                    UCI.instance.putCommand("info currmove " + savedMove.toStr());
                }
                moves = reOrderMoves(rslt.moveScores);

            }
        } catch (TimeoutException te) {
            return savedMove;
        } finally {
            negaMaxAlphaBeta.reset();
        }

        return savedMove;
    }

    public static MoveList reOrderMoves(List<MoveScore> rslt) {
        // order highest scores for us first:
        rslt.sort((o1, o2) -> o2.score - o1.score);
        List<Move> list = rslt.stream().map(s -> s.move).collect(toList());
        return new BasicMoveList(list);
    }


}
