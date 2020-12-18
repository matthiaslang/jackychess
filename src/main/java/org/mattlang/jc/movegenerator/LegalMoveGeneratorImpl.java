package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;

public class LegalMoveGeneratorImpl implements LegalMoveGenerator{

    MoveGenerator generator = Factory.getDefaults().moveGenerator.create();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, moves, side == Color.WHITE ? Color.BLACK : Color.WHITE);
        // simple ordering by capture first, to be a bit bettr in alpha beta pruning
        legalMoves.sortByCapture();
        return legalMoves;
    }

    private MoveList filterLegalMoves(BoardRepresentation currBoard, MoveList moves, Color color) {
        Figure king = color == Color.WHITE ? Figure.B_King : Figure.W_King;

        MoveList legals = Factory.getDefaults().moveList.create();
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            MoveList responseMoves = generator.generate(currBoard, color);
            moveCursor.undoMove(currBoard);

            if (!responseMoves.capturesFigure(king)) {
                legals.addMove(moveCursor);
            }

        }
        return legals;
    }

}
