package org.mattlang.jc.board;

public class RochadeMove extends BasicMove {

    public static final RochadeMove createCastlingWhiteLong() {
        return new RochadeMove(4, 2, 0, 3);
    }

    public static final RochadeMove createCastlingWhiteShort() {
       return new RochadeMove(4, 6, 7, 5);
    }

    public static final RochadeMove createCastlingBlackShort() {
        return new RochadeMove(60, 62, 63, 61);
    }

    public static final RochadeMove createCastlingBlackLong() {
        return new RochadeMove(60, 58, 56, 59);
    }



    private BasicMove second;

    private RochadeMove(int from1, int to1, int from2, int to2) {
        super(from1, to1, (byte)0);
        second = new BasicMove(from2, to2, (byte)0);
    }

    @Override
    public void move(BoardRepresentation board) {
        super.move(board);

        board.move(second.getFromIndex(), second.getToIndex());
    }

    @Override
    public void undo(BoardRepresentation board) {
        board.move(second.getToIndex(), second.getFromIndex());
        super.undo(board);
    }
}
