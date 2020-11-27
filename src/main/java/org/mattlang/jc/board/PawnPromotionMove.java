package org.mattlang.jc.board;

public class PawnPromotionMove extends BasicMove {

    public PawnPromotionMove(int from, int to) {
        super(from, to);
    }

    @Override
    public Move move(Board board) {
        Figure pawn = board.getPos(getFromIndex());
        byte override = board.move(getFromIndex(), getToIndex());

        Figure queen = pawn.color == Color.WHITE ? Figure.W_Queen : Figure.B_Queen;
        board.setPos(getToIndex(), queen);
        return new UndoPawnPromotionMove(getToIndex(), getFromIndex(), override);

    }
}
