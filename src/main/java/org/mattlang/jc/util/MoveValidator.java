package org.mattlang.jc.util;

import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.movegenerator.BBLegalMoveGeneratorImpl;

/**
 * Helper class for debugging.
 * We currently seem to have a problem that calculated pv moves are not always valid. Cutechess is logging this from
 * time to time.
 * So we check this and log at least the issue for further analysis.
 */
public class MoveValidator {

    private static final Logger LOGGER = Logger.getLogger(MoveValidator.class.getSimpleName());

    private static BBLegalMoveGeneratorImpl legalMoveGen = new BBLegalMoveGeneratorImpl();

    public static void validate(GameState gameState, NegaMaxResult rslt) {
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        for (Move move : rslt.pvList.getPvMoves()) {

            boolean legal = isLegalMove(board, move, who2Move);

            if (legal) {
                board.domove(move);
            } else {
                LOGGER.warning("Illegal PV Move " + move.toStr());

            }
            who2Move = who2Move.invert();
        }

        // test the best move itself:
        board = gameState.getBoard().copy();
        boolean legal = isLegalMove(board, rslt.savedMove, gameState.getWho2Move());
        if (!legal){
            LOGGER.warning("Illegal Best Move " + rslt.savedMove.toStr());
        }
    }

    private static boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {

        MoveList legalMoves =
                legalMoveGen.generate(board, who2Move);

        // todo clean up and make nicer way to check this...
        for (MoveCursor legalMove : legalMoves) {
            if (legalMove.getMoveInt() == move.toInt()) {
                return true;
            }
        }
        //        String moves = "";
        //        for (MoveCursor legalMove : legalMoves) {
        //            moves += new MoveImpl(legalMove.getMoveInt()).toStr();
        //            moves += ",";
        //
        //        }
        return false;
    }
}
