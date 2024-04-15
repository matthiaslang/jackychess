package org.mattlang.jc.tools;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

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

        // filter all legal moves
        MoveCursor iterator = createCursor(moveList);
        while (iterator.hasNext()) {
            iterator.next();

            board.domove(iterator);
            if (!checkChecker.isInChess(board, color)) {

                result.addMove(iterator.getMoveInt());
            }
            board.undo(iterator);
        }

        return result;
    }

    public static final MoveCursor createCursor(MoveList moveList) {
        LazySortedMoveCursorImpl cursor = new LazySortedMoveCursorImpl();
        cursor.init(moveList);
        return cursor;
    }
}
