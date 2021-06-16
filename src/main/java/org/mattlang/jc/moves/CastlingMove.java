package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;

import lombok.Getter;

@Getter
public class CastlingMove {

    private byte type;

    private byte fromIndex;

    private byte toIndex;

    private byte fromIndex2;

    private byte toIndex2;

    public static final CastlingMove CASTLING_WHITE_LONG = new CastlingMove(
            MoveImpl.CASTLING_WHITE_LONG, 4, 2, 0, 3);

    public static final CastlingMove CASTLING_WHITE_SHORT = new CastlingMove(
            MoveImpl.CASTLING_WHITE_SHORT, 4, 6, 7, 5);

    public static final CastlingMove CASTLING_BLACK_SHORT = new CastlingMove(
            MoveImpl.CASTLING_BLACK_SHORT, 60, 62, 63, 61);

    public static final CastlingMove CASTLING_BLACK_LONG = new CastlingMove(
            MoveImpl.CASTLING_BLACK_LONG, 60, 58, 56, 59);

    private CastlingMove(byte type, int fromIndex, int toIndex, int fromIndex2, int toIndex2) {
        this.type = type;
        this.fromIndex = (byte) fromIndex;
        this.toIndex = (byte) toIndex;
        this.fromIndex2 = (byte) fromIndex2;
        this.toIndex2 = (byte) toIndex2;
    }

    public final void moveSecond(BoardRepresentation board) {
        board.move(fromIndex2, toIndex2);
    }

    public final void undoSecond(BoardRepresentation board) {
        board.move(toIndex2, fromIndex2);
    }
}
