package org.mattlang.jc.engine.search;

public class TTEntry {

    public enum TTType{
        EXACT_VALUE, LOWERBOUND, UPPERBOUND
    }

    public final int value;

    public final TTType type;

    public final int depth;

    public TTEntry(int value, TTType type, int depth) {
        this.value = value;
        this.type = type;
        this.depth = depth;
    }
}
