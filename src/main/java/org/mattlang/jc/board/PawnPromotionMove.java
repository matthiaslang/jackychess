package org.mattlang.jc.board;

public class PawnPromotionMove extends BasicMove {

    private final Figure promotedFigure;

    public PawnPromotionMove(int from, int to, byte capturedFigure, Figure promotedFigure) {
        super(FigureConstants.FT_PAWN, from, to, capturedFigure);
        this.promotedFigure = promotedFigure;
    }

    @Override
    public void move(BoardRepresentation board) {
        Figure pawn = board.getPos(getFromIndex());
        byte override = board.move(getFromIndex(), getToIndex());

        board.setPos(getToIndex(), promotedFigure);
    }

    public Figure getPromotedFigure() {
        return promotedFigure;
    }

    @Override
    public void undo(BoardRepresentation board) {
        super.undo(board);

        Figure promotedFigure = board.getFigure(getFromIndex());
        Figure pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
        board.setPos(getFromIndex(), pawn);
    }
}
