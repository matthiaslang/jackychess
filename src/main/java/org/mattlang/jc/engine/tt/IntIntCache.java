package org.mattlang.jc.engine.tt;

/**
 * A thread safe tt cache with an int as payload and an int storing the zobrist key (partial).
 */
public final class IntIntCache {

    private final int shifts;
    private int capacity;

    public static final int NORESULT = 1000000;

    private int[] values;

    public IntIntCache(int bitSize) {
        capacity = 1 << (bitSize);
        values = new int[capacity * 2];

        shifts = 64 - bitSize;
    }

    public int find(long zobristHash) {
        int index = h0(zobristHash);
        int partialZ = h1(zobristHash);
        long xorKey = values[index];
        int score = values[index + 1];

        if (xorKey !=0L && (xorKey ^ score) == partialZ) {
            return score;
        }

        return NORESULT;
    }

    private int h0(long key) {
        return (int) (key >>> shifts) << 1;
    }

    private int h1(long key) {
        return (int) ((key >> 32) & (capacity - 1));
    }

    public void save(long zobristHash, int score) {
        int index = h0(zobristHash);
        int partialZ = h1(zobristHash);

        values[index] = partialZ ^ score;
        values[index + 1] = score;

    }
}
