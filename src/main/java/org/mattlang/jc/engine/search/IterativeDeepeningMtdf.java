package org.mattlang.jc.engine.search;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.NegaMaxAlphaBeta.NegaMaxResult;
import org.mattlang.jc.uci.UCI;

import java.util.logging.Logger;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.BETA_START;

/**
 * mtf implementation.
 * currently very, very slow, because alpha beta results are not save in transpostion table...
 * works also not well when sorting moves (why??)
 */
public class IterativeDeepeningMtdf implements SearchMethod {

    Logger logger = Logger.getLogger("MTDF");

    private NegaMaxAlphaBeta negaMaxAlphaBeta = new NegaMaxAlphaBeta();

    private int maxDepth ;

    private long timeout = Factory.getDefaults().getTimeout();

    @Override
    public Move search(GameState gameState, int depth) {
        negaMaxAlphaBeta.reset();
        this.maxDepth = depth;
        int currdepth = 1;
        Move savedMove = null;

        long stopTime = System.currentTimeMillis() + timeout;

        BoardRepresentation currBoard = gameState.getBoard();
        Color color = gameState.getWho2Move();
        MoveList moves = negaMaxAlphaBeta.generateMoves(currBoard, color);
        try {
            int firstguess = 0;
            for (currdepth = 1; currdepth <= maxDepth; currdepth++) {

                NegaMaxResult rslt =
                        mtdf(currBoard, currdepth, color, moves, stopTime, firstguess);

                savedMove = negaMaxAlphaBeta.getSavedMove();
                firstguess = rslt.max;

                if (savedMove != null) {
                    UCI.instance.putCommand("info depth " + currdepth + " score cp " + firstguess + " nodes " + negaMaxAlphaBeta.getNodes());
                    UCI.instance.putCommand("info currmove " + savedMove.toStr());
                }
                //moves = reOrderMoves(rslt.moveScores);

            }
        } catch (TimeoutException te) {
            return savedMove;
        } finally {
            negaMaxAlphaBeta.reset();
        }

        return savedMove;
    }

    NegaMaxResult mtdf(BoardRepresentation board, int depth, Color color,  MoveList moves,long stopTime,int f) {
        int lower = ALPHA_START;
        int upper = BETA_START;
        NegaMaxResult result = null;
        int rounds = 0;
        do {
            rounds++;
            int beta = f;
            if (f == lower) {
                beta += 1;
            }
            //logger.info("alpha: " + (beta -1) + " beta: " + beta);
            result = negaMaxAlphaBeta.searchWithScore(board, depth, color, beta - 1, beta, moves, stopTime);
            f = result.max;
            if (f < beta) {
                upper = f;
            }else {
                lower = f;
            }
        } while (lower < upper);

        UCI.instance.putCommand("info depth " + depth + " mtdf: rounds: " + rounds + " score cp:" + f + " lower: " + lower + " upper: " + upper );

        return result;
    }

}
