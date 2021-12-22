package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.Color;

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
        if (zobrists[index] == zobristHash) {
            return scores[index];
        }

        return NORESULT;
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    public void save(long zobristHash, Color who2Move, int score) {
        int index = h0(zobristHash);

        zobrists[index] = zobristHash;
        scores[index] = score;

    }
}
