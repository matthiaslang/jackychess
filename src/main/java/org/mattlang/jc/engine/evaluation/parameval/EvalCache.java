package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.Color;

/**
 * A thread safe eval cache.
 */
public class EvalCache {

    public static final int bitSize = 24;

    public static final int CAPACITY = 1 << bitSize;
    public static final int NORESULT = 1000000;

    private long[] zobrists = new long[CAPACITY];
    private int[] scores = new int[CAPACITY];

    public final static EvalCache instance = new EvalCache();

    private EvalCache() {

    }

    public int find(long zobristHash, Color who2Move) {
        int index = h0(zobristHash);
        long xorKey =   zobrists[index];
        int score = scores[index];

        if ((xorKey ^ score) == zobristHash) {
            return score;
        }

        return NORESULT;
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    public void save(long zobristHash, Color who2Move, int score) {
        int index = h0(zobristHash);

        zobrists[index] = zobristHash ^ score;
        scores[index] = score;

    }
}
