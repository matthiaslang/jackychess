package org.mattlang.jc.engine.tt;

import lombok.Getter;

/**
 * TT Entry.
 *
 * Saving score, type and maybe hash move of a position.
 */
public final class TTEntry {

    public static final byte EXACT_VALUE = 1;
    public static final byte LOWERBOUND = 2;
    public static final byte UPPERBOUND = 3;

    /**
     * zobrist hash. 0 means empty entry.
     */
    long zobristHash;

    @Getter
    private short value;

    private byte type;

    @Getter
    int depth;

    @Getter
    byte aging;

    @Getter
    int move;

    public TTEntry(long zobristHash, int value, byte type, int depth, byte aging, int move) {
        this.zobristHash = zobristHash;
        this.value = (short)value;
        this.type = type;
        this.depth = depth;
        this.aging = aging;
        this.move = move;
    }

    public boolean isExact() {
        return type == EXACT_VALUE;
    }

    public boolean isLowerBound() {
        return type == LOWERBOUND;
    }

    public boolean isUpperBound() {
        return type == UPPERBOUND;
    }

    public boolean isEmpty() {
        return zobristHash ==0L;
    }

    void update(long zobristHash, int value, byte type, int depth, byte aging, int move) {
        this.zobristHash = zobristHash;
        this.value = (short)value;
        this.type = type;
        this.depth = depth;
        this.aging = aging;
        if (this.move == 0 || move != 0) {
            this.move = move;
        }
    }

    @Override
    public String toString() {
        return "TTEntry{" +
                "zobristHash=" + zobristHash +
                ", value=" + value +
                ", type=" + type +
                ", depth=" + depth +
                ", aging=" + aging +
                ", move=" + move +
                '}';
    }

    /**
     * Does this entry has a lower type than the given?
     * Exact values have highest value, all other are not so relevant.
     *
     * @param tpe
     * @return
     */
    public boolean isLower(byte tpe) {
        return tpe == EXACT_VALUE && this.type != EXACT_VALUE;
    }
}
