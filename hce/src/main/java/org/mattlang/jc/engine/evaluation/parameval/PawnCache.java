package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.engine.search.ContextCache;

/**
 * A thread safe tt cache with an int as payload and an int storing the zobrist key (partial).
 */
public final class PawnCache implements ContextCache {

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
        if (entry.zobrist != zobristHash) {
            return null;
        }
        return entry;
    }

    private int h0(long key) {
        return (int) (key & (capacity - 1));
    }

    public void save(long zobristHash, EvalResult result) {
        int index = h0(zobristHash);

        PawnCacheEntry entry = entries[index];

        entry.zobrist = zobristHash;
        entry.score = result.pawnEval;
        entry.whitePassers = result.whitePassers;
        entry.blackPassers = result.blackPassers;
        entry.pkeval = result.pkeval;
        System.arraycopy(result.pksafety, 0, entry.pksafety, 0, 2);
    }

    @Override
    public void reset() {
        for (int i = 0; i < capacity; i++) {
            entries[i].reset();
        }
    }
}
