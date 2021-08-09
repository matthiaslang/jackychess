package org.mattlang.jc.engine.tt;

import lombok.Getter;

public class TTEntry<T> {

    public static final byte EMPTY = 0;
    public static final byte EXACT_VALUE = 1;
    public static final byte LOWERBOUND = 2;
    public static final byte UPPERBOUND = 3;

    long zobristHash;

    @Getter
    private int value;

    private byte type;

    @Getter
    int depth;

    @Getter
    byte aging;

    public TTEntry(long zobristHash, int value, byte type, int depth) {
        this.zobristHash = zobristHash;
        this.value = value;
        this.type = type;
        this.depth = depth;
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
        return type == EMPTY;
    }

    void update(long zobristHash, int value, byte type, int depth, byte aging) {
        this.zobristHash = zobristHash;
        this.value = value;
        this.type = type;
        this.depth = depth;
        this.aging = aging;
    }

    @Override
    public String toString() {
        return "TTEntry{" +
                "zobristHash=" + zobristHash +
                ", value=" + value +
                ", type=" + type +
                ", depth=" + depth +
                ", aging=" + aging +
                '}';
    }
}
