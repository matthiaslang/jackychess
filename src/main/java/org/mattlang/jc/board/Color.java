package org.mattlang.jc.board;

public enum Color {
    WHITE((byte) 0),
    BLACK((byte) 64);

    public final byte code;

    Color(byte code) {
        this.code = code;
    }
}
