package org.mattlang.jc.tools;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.Captures;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveImpl;

public class LegalMoves {

    /**
     * Delivers a move list with all legal moves for a position and a side to move.
     * This uses the PseudoLegalMoveGenerator + iterator + checkchecker to filter all legal moves.
     *
     * Used in Tests for validation.
     *
     * @param board
     * @param color
     * @return
     */
    public static MoveList generateLegalMoves(BoardRepresentation board, Color color) {
        MoveList moveList = new MoveList();
        moveList.reset(color.ordinal());
        PseudoLegalMoveGenerator movegen = new PseudoLegalMoveGenerator();

        movegen.generate(board, color.ordinal(), moveList);

        MoveList result = new MoveList();
        MoveImpl wrapper = new MoveImpl("a1a1");
        // filter all legal moves
        for (int i = 0; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            wrapper.fromLongEncoded(moveInt);

            board.domove(wrapper);
            if (!Captures.canKingCaptured(board, color.ordinal())) {

                result.addMove(moveInt);
            }
            board.undo(wrapper);
        }

        return result;
    }

}
