package org.mattlang.jc.board;

public class EnPassantMove extends BasicMove {

    private int enPassantCapturePos;

    public EnPassantMove(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        super(from, to, capturedFigure);
        this.enPassantCapturePos = enPassantCapturePos;
    }

    @Override
    public Move move(BoardRepresentation board) {
        super.move(board);
        board.setPos(enPassantCapturePos, FigureConstants.FT_EMPTY);

        return new EnPassantUndoMove(getToIndex(), getFromIndex(), getCapturedFigure(), enPassantCapturePos);
    }
}
