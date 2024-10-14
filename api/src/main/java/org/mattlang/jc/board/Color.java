package org.mattlang.jc.board;

public enum Color {
    WHITE((byte) 0),
    BLACK((byte) 8);

    public static final int nWhite = Color.WHITE.ordinal();
    public static final int nBlack = Color.BLACK.ordinal();

    public final byte code;

    Color(byte code) {
        this.code = code;
    }

    public final Color invert() {
        return this == WHITE ? BLACK : WHITE;
    }

    public static final int invert(int color) {
        return 1 - color;
    }
}
