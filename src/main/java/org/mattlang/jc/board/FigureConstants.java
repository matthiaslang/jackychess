package org.mattlang.jc.board;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

public class FigureConstants {

    public static final byte FT_EMPTY = 7;
    public static final byte FT_PAWN = 0;
    public static final byte FT_KNIGHT = 1;
    public static final byte FT_BISHOP = 2;
    public static final byte FT_ROOK = 3;
    public static final byte FT_QUEEN = 4;
    public static final byte FT_KING = 5;

    public static final byte W_PAWN = (byte) (FT_PAWN | Color.WHITE.code);
    public static final byte W_KNIGHT = (byte) (FT_KNIGHT | Color.WHITE.code);
    public static final byte W_BISHOP = (byte) (FT_BISHOP | Color.WHITE.code);
    public static final byte W_ROOK = (byte) (FT_ROOK | Color.WHITE.code);
    public static final byte W_QUEEN = (byte) (FT_QUEEN | Color.WHITE.code);
    public static final byte W_KING = (byte) (FT_KING | Color.WHITE.code);

    public static final byte B_PAWN = (byte) (FT_PAWN | Color.BLACK.code);
    public static final byte B_KNIGHT = (byte) (FT_KNIGHT | Color.BLACK.code);
    public static final byte B_BISHOP = (byte) (FT_BISHOP | Color.BLACK.code);
    public static final byte B_ROOK = (byte) (FT_ROOK | Color.BLACK.code);
    public static final byte B_QUEEN = (byte) (FT_QUEEN | Color.BLACK.code);
    public static final byte B_KING = (byte) (FT_KING | Color.BLACK.code);

    public static final byte MASK_OUT_COLOR=(byte)(0xFF- WHITE.code - BLACK.code);
}
