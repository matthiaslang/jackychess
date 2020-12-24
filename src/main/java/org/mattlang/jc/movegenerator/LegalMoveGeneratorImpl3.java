package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;

public class LegalMoveGeneratorImpl3 implements LegalMoveGenerator {

    MoveGenerator generator = Factory.getDefaults().moveGenerator.create();

    MoveGeneratorImpl2 moveGeneratorImpl2 = new MoveGeneratorImpl2();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, moves, side.invert());
        // simple ordering by capture first, to be a bit bettr in alpha beta pruning
        legalMoves.sortByCapture();
        return legalMoves;
    }

    private MoveList filterLegalMoves(BoardRepresentation currBoard, MoveList moves, Color color) {
        Color otherColor = color.invert();

        Board2 board2 = (Board2) currBoard;

        MoveList legals = Factory.getDefaults().moveList.create();

        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);

            if (!isInChess(board2, otherColor)) {
                legals.addMove(moveCursor);
            }
            moveCursor.undoMove(currBoard);
        }
        return legals;
    }

    /**
     * Is the other colors king in check?
     * @param otherColor
     * @return
     */
    private boolean isInChess(Board2 board2, Color otherColor) {
        PieceList otherPieces = otherColor == Color.WHITE ? board2.getWhitePieces() : board2.getBlackPieces();
        int kingPos = otherPieces.getKing();
        return moveGeneratorImpl2.canFigureCaptured(board2, kingPos);
    }
}
