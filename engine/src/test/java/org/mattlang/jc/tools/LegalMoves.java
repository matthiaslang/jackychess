package org.mattlang.jc.tools;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
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
        moveList.reset(color);
        PseudoLegalMoveGenerator movegen = new PseudoLegalMoveGenerator();
        CheckChecker checkChecker = new BBCheckCheckerImpl();

        movegen.generate(board, color, moveList);

        MoveList result = new MoveList();
        MoveImpl wrapper = new MoveImpl("a1a1");
        // filter all legal moves
        for (int i = 0; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            wrapper.fromLongEncoded(moveInt);

            board.domove(wrapper);
            if (!checkChecker.isInChess(board, color)) {

                result.addMove(moveInt);
            }
            board.undo(wrapper);
        }

        return result;
    }

}
