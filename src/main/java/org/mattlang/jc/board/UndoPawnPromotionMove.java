package org.mattlang.jc.board;

public class UndoPawnPromotionMove extends BasicMove {

    /**
     * the overriden field/figure by the move, which needs to be reset during the undoing.
     */
    public final byte overriddenFig;

    public UndoPawnPromotionMove(int toIndex, int fromIndex, byte override) {
        super(toIndex, fromIndex, (byte)0);
        this.overriddenFig = override;
    }

    @Override
    public Move move(BoardRepresentation board) {
        super.move(board);

        board.setPos(getFromIndex(), overriddenFig);

        Figure queen = board.getFigure(getToIndex());
        Figure pawn = queen.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
        board.setPos(getToIndex(), pawn);

        return null;

    }
}
