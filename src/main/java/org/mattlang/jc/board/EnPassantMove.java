package org.mattlang.jc.board;

import java.util.Objects;

public class EnPassantMove extends BasicMove {

    private int enPassantCapturePos;

    public EnPassantMove(int from, int to, byte capturedFigure, int enPassantCapturePos) {
        super(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.enPassantCapturePos = enPassantCapturePos;
    }

    @Override
    public void move(BoardRepresentation board) {
        super.move(board);
        board.setPos(enPassantCapturePos, FigureConstants.FT_EMPTY);

    }

    @Override
    public void undo(BoardRepresentation board) {
        super.undo(board);

        // override the "default" overrider field with empty..
        board.setPos(getToIndex(), FigureConstants.FT_EMPTY);
        // because we have the special en passant capture pos which we need to reset with the captured figure
        board.setPos(enPassantCapturePos, getCapturedFigure());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        EnPassantMove that = (EnPassantMove) o;
        return enPassantCapturePos == that.enPassantCapturePos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enPassantCapturePos);
    }
}
