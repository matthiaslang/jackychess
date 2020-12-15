package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.NegaMaxAlphaBeta.MoveScore;
import org.mattlang.jc.uci.UCI;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.BETA_START;

public class IterativeDeepeningMtdf implements SearchMethod {


    private NegaMaxAlphaBeta negaMaxAlphaBeta = new NegaMaxAlphaBeta();

    private int maxDepth = 12;

    private long timeout = 15 * 1000;


    public Move search(Board currBoard, int depth, Color color) {
        negaMaxAlphaBeta.reset();
        this.maxDepth = depth;
        int currdepth = 1;
        Move savedMove = null;

        long stopTime = System.currentTimeMillis() + timeout;

        MoveList moves = negaMaxAlphaBeta.generateMoves(currBoard, color);
        try {
            int firstguess = 0;
            for (currdepth = 1; currdepth <= maxDepth; currdepth++) {

                List<MoveScore> rslt =
                        mtdf(currBoard, currdepth, color, moves, stopTime, firstguess);

                savedMove = negaMaxAlphaBeta.getSavedMove();
                firstguess = negaMaxAlphaBeta.getSavedMoveScore();

                if (savedMove != null) {
                    UCI.instance.putCommand("info depth " + currdepth + " score cp " + firstguess + " nodes " + negaMaxAlphaBeta.getNodes());
                    UCI.instance.putCommand("info currmove " + savedMove.toStr());
                }
                moves = reOrderMoves(rslt);

            }
        } catch (TimeoutException te) {
            return savedMove;
        } finally {
            negaMaxAlphaBeta.reset();
        }

        return savedMove;
    }

    private MoveList reOrderMoves(List<MoveScore> rslt) {
        // order highest scores for us first:
        rslt.sort((o1, o2) -> o2.score - o1.score);
        List<Move> list = rslt.stream().map(s -> s.move).collect(toList());
        return new BasicMoveList(list);
    }

    List<MoveScore> mtdf(Board board, int depth, Color color,  MoveList moves,long stopTime,int f) {
        int lower = ALPHA_START;
        int upper = BETA_START;
        List<MoveScore> result = null;
        do {
            int beta = f;
            if (f == lower) {
                beta += 1;
            }
            result = negaMaxAlphaBeta.searchWithScore(board, depth, color, beta - 1, beta, moves, stopTime);
            f = negaMaxAlphaBeta.getSavedMoveScore();
            if (f < beta) {
                upper = f;
            }else {
                lower = f;
            }
        } while (lower < upper);

        UCI.instance.putCommand("info depth " + depth + " mtdf: score cp " + f + " mtdf: lower : " + lower + " upper: " + upper );

        return result;
    }

}
