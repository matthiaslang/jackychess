package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;

public class LegalMoveGeneratorImpl3 implements LegalMoveGenerator {

    MoveGenerator generator = Factory.getDefaults().moveGenerator.instance();

    CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, moves, side);
        return legalMoves;
    }

    private MoveList filterLegalMoves(BoardRepresentation currBoard, MoveList moves, Color side) {

        MoveList legals = Factory.getDefaults().moveList.create();

        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);

            if (!checkChecker.isInChess(currBoard, side)) {
                legals.addMove(moveCursor);
            }
            moveCursor.undoMove(currBoard);
        }
        return legals;
    }

}
