package org.mattlang.jc.board;

public class PawnPromotionMove extends BasicMove {

    private final Figure promotedFigure;

    // todo save promotion figure..

    public PawnPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        super(from, to, capturedFigure);
        this.promotedFigure = promotedFigure;
    }

    @Override
    public Move move(BoardRepresentation board) {
        Figure pawn = board.getPos(getFromIndex());
        byte override = board.move(getFromIndex(), getToIndex());

        board.setPos(getToIndex(), promotedFigure);
        return new UndoPawnPromotionMove(getToIndex(), getFromIndex(), override);


    }
}
