package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.engine.tt.TTEntry.*;

import lombok.Data;

/**
 * Resultclass to encapsulate internal cache data structures.
 */
@Data
public class TTResult {

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
