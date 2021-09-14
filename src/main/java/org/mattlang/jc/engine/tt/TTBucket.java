package org.mattlang.jc.engine.tt;

/**
 * A Bucket with entries for the same hash index of the table.
 * There is exactly only one entry for one zobrist hash in this bucket list (or none).
 * Exchanges/updates for the same zobrist hash are therefore updating the same bucket entry (if possible by other
 * conditions).
 */
public class TTBucket {

    public static final int MAX_AGE_DIFF = 2;
    private static final int MAX_BUCKET_SIZE = 10;
    private TTEntry[] bucketlist;

    /**
     * Search for a entry in the bucket list by zobrist key and current aging.
     *
     * @param boardZobristHash
     * @param currAging
     * @return the entry or null if not found or the entry is too old
     */
    public TTEntry search(long boardZobristHash, int currAging) {
        TTEntry entry = findZobristMatch(boardZobristHash);
        if (entry == null) {
            return null;
        }
        if (!entry.isEmpty() && withinAge(entry, currAging)) {
            return entry;
        }
        return null;
    }

    private boolean withinAge(TTEntry entry, int currAging) {
        return currAging - entry.getAging() < MAX_AGE_DIFF;
    }

    /**
     * Tries to find a free entry in this bucket for a zobrist hash and depth.
     *
     * @param depth
     * @param boardZobristHash
     * @param currAging
     * @return
     */
    public TTEntry findFreeEntry(int depth, long boardZobristHash, byte currAging) {

        TTEntry entry = findZobristMatch(boardZobristHash);
        if (entry == null) {
            return aquireAnyFreePlace(currAging);
        } else {
            if (entry.isEmpty()) {
                return entry;
            }
            if (!withinAge(entry, currAging)) {
                return entry;
            }
            if (entry.depth > depth) {
                return entry;
            }
            return null;
        }
    }

    /**
     * When we have not found an entry for this zobrist hash, find any free place or create a new one in the bucket list
     *
     * @param currAging
     * @return
     */
    private TTEntry aquireAnyFreePlace(byte currAging) {
        if (bucketlist == null) {
            bucketlist = new TTEntry[MAX_BUCKET_SIZE];
        }

        for (int i = 0; i < bucketlist.length; i++) {
            TTEntry entry = bucketlist[i];
            if (entry == null) {
                bucketlist[i] = new TTEntry(0, 0, TTEntry.EXACT_VALUE, 0, currAging, 0);
                return bucketlist[i];
            }
            if (entry.isEmpty()) {
                return entry;
            }
            if (!withinAge(entry, currAging)) {
                return entry;
            }
        }
        return null;
    }

    private TTEntry findZobristMatch(long boardZobristHash) {
        if (bucketlist == null) {
            return null;
        }
        for (int i = 0; i < bucketlist.length; i++) {
            TTEntry entry = bucketlist[i];
            if (entry == null) {
                return null;
            }
            if (entry.zobristHash == boardZobristHash) {
                return entry;
            }
        }
        return null;
    }
}
