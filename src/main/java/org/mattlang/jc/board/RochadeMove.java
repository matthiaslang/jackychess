package org.mattlang.jc.board;

public class RochadeMove extends BasicMove {

    public static final RochadeMove createCastlingWhiteLong(byte castlingRightsBefore) {
        return new RochadeMove(castlingRightsBefore, 4, 2, 0, 3);
    }

    public static final RochadeMove createCastlingWhiteShort(byte castlingRightsBefore) {
       return new RochadeMove(castlingRightsBefore, 4, 6, 7, 5);
    }

    public static final RochadeMove createCastlingBlackShort(byte castlingRightsBefore) {
        return new RochadeMove(castlingRightsBefore, 60, 62, 63, 61);
    }

    public static final RochadeMove createCastlingBlackLong(byte castlingRightsBefore) {
        return new RochadeMove(castlingRightsBefore, 60, 58, 56, 59);
    }



    private BasicMove second;

    private RochadeMove(byte castlingRightsBefore, int from1, int to1, int from2, int to2) {
        super(castlingRightsBefore, from1, to1, (byte)0);
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
