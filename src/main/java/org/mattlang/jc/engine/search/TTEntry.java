package org.mattlang.jc.engine.search;

public class TTEntry<T> {

    public enum TTType {
        EXACT_VALUE,
        LOWERBOUND,
        UPPERBOUND
    }

    public final long zobristHash;

    public final int value;

    public final TTType type;

    public final int depth;

    public TTEntry(long zobristHash, int value, TTType type, int depth) {
        this.zobristHash = zobristHash;
        this.value = value;
        this.type = type;
        this.depth = depth;
    }
}
