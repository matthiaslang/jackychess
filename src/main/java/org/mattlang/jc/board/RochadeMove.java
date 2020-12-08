package org.mattlang.jc.board;

public class RochadeMove extends BasicMove {

    private BasicMove second;

    public RochadeMove(int from1, int to1, int from2, int to2) {
        super(from1, to1, (byte)0);
        second = new BasicMove(from2, to2, (byte)0);
    }

    @Override
    public Move move(Board board) {
        super.move(board);

        board.move(second.getFromIndex(), second.getToIndex());
        return new RochadeUndoMove(getToIndex(), getFromIndex(),
                second.getToIndex(), second.getFromIndex());
    }
}
