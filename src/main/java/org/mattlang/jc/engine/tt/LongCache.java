package org.mattlang.jc.engine.tt;

/**
 * A thread safe tt cache with a long as payload.
 * todo refactor other caches to use this (e.g. TTCache3)
 */
public final class LongCache {

    private int bitSize;
    private int capacity;

    public static final long NORESULT = Long.MAX_VALUE;

    private long[] zobrists;
    private long[] scores;

    public LongCache(int bitSize) {
        this.bitSize = bitSize;
        capacity = 1 << bitSize;
        zobrists = new long[capacity];
        scores = new long[capacity];
    }

    public long find(long zobristHash) {
        int index = h0(zobristHash);
        long xorKey = zobrists[index];
        long score = scores[index];

        if ((xorKey ^ score) == zobristHash) {
            return score;
        }

        return NORESULT;
    }

    private int h0(long key) {
        return (int) (key & (capacity - 1));
    }

    public void save(long zobristHash, long score) {
        int index = h0(zobristHash);

        zobrists[index] = zobristHash ^ score;
        scores[index] = score;

    }

    public static int getScore(final long value) {
        int score = (int) (value >> SCORE);
        return score;
    }

    public static int getDepth(final long value) {
        return (int) ((value & 0x3ff) - DEPTH_OFFSET);
    }

    public static int getFlag(final long value) {
        return (int) (value >>> FLAG & 3);
    }

    public static int getMove(final long value) {
        return (int) (value >>> MOVE & 0xffffffff);
    }

    // SCORE,HALF_MOVE_COUNTER,MOVE,FLAG,DEPTH
    public static long createValue(final long score, final long move, final long flag, final int depth) {
        return score << SCORE | move << MOVE | flag << FLAG | (depth + DEPTH_OFFSET);
    }

    /**
     * 8: depth
     * 8: flag
     * 16+32 = 48: move
     *
     * */

    /**
     * offset to deal correctly with negative values. todo how to impl this with real unsigned arithmetic?
     */
    private static final int DEPTH_OFFSET = 512;

    // ///////////////////// DEPTH //10 bits
    private static final int FLAG = 10; // 2
    private static final int MOVE = 12; // 22
    private static final int SCORE = 48; // 16
}
