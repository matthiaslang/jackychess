package org.mattlang.jc.engine.evaluation.parameval;

/**
 * A thread safe tt cache with an int as payload and an int storing the zobrist key (partial).
 */
public final class PawnCache {

    public static final PawnCache EMPTY_CACHE = new PawnCache(0);

    private int bitSize;
    private int capacity;

    private PawnCacheEntry[] entries;

    public PawnCache(int bitSize) {
        this.bitSize = bitSize;
        capacity = 1 << bitSize;
        entries = new PawnCacheEntry[capacity];
        for (int i = 0; i < capacity; i++) {
            entries[i] = new PawnCacheEntry();
        }
    }


    public PawnCacheEntry find(long zobristHash) {
        int index = h0(zobristHash);
        PawnCacheEntry entry = entries[index];
        if (entry.zobrist != zobristHash){
            return null;
        }
        return entry;
    }

    private int h0(long key) {
        return (int) (key & (capacity - 1));
    }

    public void save(long zobristHash, int score, long whitePassers, long blackPassers) {
        int index = h0(zobristHash);

        PawnCacheEntry entry = entries[index];

        entry.zobrist = zobristHash;
        entry.score = score;
        entry.whitePassers = whitePassers;
        entry.blackPassers = blackPassers;
    }

    public void reset() {
        for (int i = 0; i < capacity; i++) {
            entries[i].reset();
        }
    }
}
