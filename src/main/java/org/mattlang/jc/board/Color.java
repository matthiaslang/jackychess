package org.mattlang.jc.board;

public enum Color {
    WHITE((byte) 8),
    BLACK((byte) 16);

    public final byte code;

    Color(byte code) {
        this.code = code;
    }

    public final Color invert() {
        return this == WHITE ? BLACK : WHITE;
    }
}
