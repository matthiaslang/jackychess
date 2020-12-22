package org.mattlang.jc.board;

public class EnPassantUndoMove extends UndoMove {


    private final int enPassantCapturedPos;
    private final byte capturedFigure;

    public EnPassantUndoMove(int from, int to, byte capturedFigure, int enPassantCapturedPos) {
        super(from, to, FigureConstants.FT_EMPTY);
        this.enPassantCapturedPos = enPassantCapturedPos;
        this.capturedFigure = capturedFigure;
    }

    @Override
    public Move move(BoardRepresentation board) {
        super.move(board);
        board.setPos(enPassantCapturedPos, capturedFigure);
        return null;
    }
}
