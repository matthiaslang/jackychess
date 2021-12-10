package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.Color;

public class EvalCache {

    public static final int bitSize = 24;

    public static final int CAPACITY = 1 << bitSize;
    public static final int NORESULT = 1000000;

    private long[] zobristsWhite = new long[CAPACITY];
    private int[] scoresWhite = new int[CAPACITY];
    private long[] zobristsBlack = new long[CAPACITY];
    private int[] scoresBlack = new int[CAPACITY];

    public final static EvalCache instance = new EvalCache();

    private EvalCache() {

    }

    public int find(long zobristHash, Color who2Move) {
        int index = h0(zobristHash);
        switch (who2Move) {
        case WHITE:
            if (zobristsWhite[index] == zobristHash) {
                return scoresWhite[index];
            }
            break;
        case BLACK:
            if (zobristsBlack[index] == zobristHash) {
                return scoresBlack[index];
            }
            break;
        }
        return NORESULT;
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    public void save(long zobristHash, Color who2Move, int score) {
        int index = h0(zobristHash);
        switch (who2Move) {
        case WHITE:
            zobristsWhite[index] = zobristHash;
            scoresWhite[index] = score;
            break;
        case BLACK:
            zobristsBlack[index] = zobristHash;
            scoresBlack[index] = score;
            break;
        }
    }
}
