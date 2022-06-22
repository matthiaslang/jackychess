package org.mattlang.jc.engine.tt;

import lombok.Data;

/**
 * Resultclass to encapsulate internal cache data structures.
 */
@Data
public class TTResult {

    public static final byte EXACT_VALUE = 1;
    public static final byte LOWERBOUND = 2;
    public static final byte UPPERBOUND = 3;

    private byte type;
    private int depth;
    private int score;
    private int move;

    public boolean isExact() {
        return type == EXACT_VALUE;
    }

    public boolean isLowerBound() {
        return type == LOWERBOUND;
    }

    public boolean isUpperBound() {
        return type == UPPERBOUND;
    }
}
