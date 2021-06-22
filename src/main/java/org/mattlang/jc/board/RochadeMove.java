package org.mattlang.jc.board;

import java.util.Objects;

public class RochadeMove extends BasicMove {

    public static final RochadeMove CASTLING_WHITE_LONG = new RochadeMove(4, 2, 0, 3);

    public static final RochadeMove CASTLING_WHITE_SHORT = new RochadeMove(4, 6, 7, 5);

    public static final RochadeMove CASTLING_BLACK_SHORT = new RochadeMove(60, 62, 63, 61);

    public static final RochadeMove CASTLING_BLACK_LONG = new RochadeMove(60, 58, 56, 59);


    private BasicMove second;

    private RochadeMove(int from1, int to1, int from2, int to2) {
        super(FigureConstants.FT_KING, from1, to1, (byte)0);
        second = new BasicMove(FigureConstants.FT_ROOK, from2, to2, (byte)0);
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

    @Override
    public boolean isCastling() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        RochadeMove that = (RochadeMove) o;
        return second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), second);
    }
}
