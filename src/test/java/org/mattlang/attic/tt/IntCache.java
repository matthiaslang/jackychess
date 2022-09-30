package org.mattlang.attic.tt;

/**
 * A thread safe tt cache with an int as payload.
 */
public final class IntCache {

    private int bitSize;
    private int capacity;

    public static final int NORESULT = Integer.MAX_VALUE;

    private long[] zobrists;
    private int[] scores;

    public IntCache(int bitSize) {
        this.bitSize = bitSize;
        capacity = 1 << bitSize;
        zobrists = new long[capacity];
        scores = new int[capacity];
    }

    public int find(long zobristHash) {
        int index = h0(zobristHash);
        long xorKey = zobrists[index];
        int score = scores[index];

        if ((xorKey ^ score) == zobristHash) {
            return score;
        }

        return NORESULT;
    }

    private int h0(long key) {
        return (int) (key & (capacity - 1));
    }

    public void save(long zobristHash, int score) {
        int index = h0(zobristHash);

        zobrists[index] = zobristHash ^ score;
        scores[index] = score;

    }
}
