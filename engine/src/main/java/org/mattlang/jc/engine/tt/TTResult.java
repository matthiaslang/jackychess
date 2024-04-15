package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.engine.evaluation.Weights.KING_WEIGHT;

import lombok.Data;

/**
 * Resultclass to encapsulate internal cache data structures.
 */
@Data
public final class TTResult {

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

    public static final byte toFlag(int max, int alpha, int beta) {
        if (max <= alpha) // value below alpha, so we can only use it as an upper bound
            return UPPERBOUND;
        else if (max >= beta) // higher beta, so we can only use it as an lower bound
            return LOWERBOUND;
        else // a true minimax value
            return EXACT_VALUE;
    }

    /**
     * Adjust Mate scores by the ply.
     *
     * We do this by adjusting it with the current given ply.
     *
     * @param ply
     * @return
     */
    public int getAdjustedScore(int ply) {
        int sc = score;
        if (sc > KING_WEIGHT - 1000) {
            sc = KING_WEIGHT - ply;
        } else if (sc < -(KING_WEIGHT - 1000)) {
            sc = -KING_WEIGHT + ply;
        }
        return sc;
    }
}
