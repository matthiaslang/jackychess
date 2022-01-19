package org.mattlang.jc.engine.tt;

import lombok.Getter;

/**
 * TT Entry.
 *
 * Saving score, type and maybe hash move of a position.
 */
@Getter
public final class TTE {

    public static final byte EXACT_VALUE = 1;
    public static final byte LOWERBOUND = 2;
    public static final byte UPPERBOUND = 3;

    /**
     * zobrist hash.
     */
    long zobristHash;

    @Getter
    private short value;

    private byte type;

    @Getter
    short depth;

    @Getter
    int move;

    public TTE(long zobristHash, int value, byte type, int depth, int move) {
        this.zobristHash = zobristHash;
        this.value = (short) value;
        this.type = type;
        this.depth = (short) depth;
        this.move = move;
    }

    void update(long zobristHash, int value, byte type, int depth, int move) {
        this.zobristHash = zobristHash;
        this.value = (short) value;
        this.type = type;
        this.depth = (short) depth;
        this.move = move;
    }

}
