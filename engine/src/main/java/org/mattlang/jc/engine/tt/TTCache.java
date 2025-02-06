package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.Constants.DEFAULT_CACHE_SIZE_MB;
import static org.mattlang.jc.engine.tt.TTResult.toFlag;

import java.util.Arrays;
import java.util.logging.Logger;

import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

import lombok.Getter;

/**
 * Cache using only a long array to be faster and more memory efficient.
 * It is also thread-safe (thread consistent for read access), so that it could be used for a Lazy-SMP Search algorithm.
 * It combines a "always save" with replace "lowest depth" in a multi bucket cache.
 */
public final class TTCache {

    public static final long NORESULT = Long.MAX_VALUE;

    private static final Logger LOGGER = Logger.getLogger(TTCache.class.getSimpleName());

    private static final int BUCKET_SIZE = 3;

    private int keyShifts;

    // key, value
    private long[] keys;

    private TTAging aging = new TTAging();

    private int halfMoveCounter = 0;

    /**
     * 8: depth
     * 8: flag
     * 16+32 = 48: move
     */

    // ///////////////////// DEPTH //10 bits
    private static final int FLAG = 10; // 2
    private static final int MOVE = 12; // 22
    private static final int SCORE = 48; // 16

    @Getter
    private long cacheHits = 0;
    @Getter
    private long cacheMisses = 0;

    @Getter
    private int numFilledEntries = 0;
    @Getter
    private int noReplaceCacheAlreadyBetter;

    private int mbSize = DEFAULT_CACHE_SIZE_MB;

    /**
     * no. of "places" in the index. effectively the array size div bucketChunkSize.
     */
    private int indexPlaces;

    /**
     * Size of a "chunk" consisting of the data of all buckets of an index place.
     */
    private int bucketChunkSize;

    private int determineBitSizeFromConfig() {
        int mb = getConfiguredMbSize();
        mbSize = mb;
        return determineCacheBitSizeFromMb(mb, 16);
    }

    private int getConfiguredMbSize() {
        Integer mb = ConfigValues.getConfigValues().hash.getValue();

        if (mb == null) {
            return DEFAULT_CACHE_SIZE_MB;
        }
        return mb.intValue();
    }

    public static int determineCacheBitSizeFromMb(int mb, int sizeOfSlot) {
        int slots = mb * 1024 * 1024 / sizeOfSlot;
        int bits = (int) (Math.log(slots) / Math.log(2));
        LOGGER.info("cache of " + mb + "MB: setting cache to " + slots + " slots, " + bits + " bits");
        return bits;
    }

    public TTCache() {
        initCache();
    }

    private void initCache() {
        int bitSize = determineBitSizeFromConfig();
        int power2TtEntries = bitSize - BUCKET_SIZE + 1;

        keyShifts = 64 - power2TtEntries;
        int maxEntries = (int) (1L << bitSize) * 2;
        LOGGER.info("TT Cache: allocating " + maxEntries + " longs;");

        keys = new long[maxEntries];
        bucketChunkSize = BUCKET_SIZE * 2;
        // entries is size div bucket size div data size.
        indexPlaces = keys.length / bucketChunkSize;
    }

    public void reset() {
        Arrays.fill(keys, 0);
        halfMoveCounter = 0;
        numFilledEntries = 0;
        resetStatistics();
        aging.reset();
        checkUpdateCacheSize();
    }

    public long getValue(final long key) {

        final int index = getIndex(key);

        for (int i = index; i < index + BUCKET_SIZE * 2; i += 2) {
            long xorKey = keys[i];
            long value = keys[i + 1];
            if ((xorKey ^ value) == key) {
                cacheHits++;
                return value;
            }
        }

        cacheMisses++;
        return NORESULT;
    }

    /**
     * Calculates the index place in the data.
     * @param key
     * @return
     */
    private int getIndex(final long key) {
        // xor upper and lower halves of zobrist together and apply mask to have positive values:
        long index = (key ^ (key >>> 32)) & 0x7FFFFFFF;
        // use modulo to ensure better distribution of the values over the array.
        return (int) (index % indexPlaces) * bucketChunkSize;
    }

    public void addValue(final long key, int score, final int depth, final int flag, final int move) {

        final int index = getIndex(key);
        long replacedDepth = Integer.MAX_VALUE;
        int replaceIndex = index;
        for (int i = index; i < index + BUCKET_SIZE * 2; i += 2) {

            long xorKey = keys[i];
            if (xorKey == 0) {
                replaceIndex = i;
                numFilledEntries++;
                break;
            }

            long currentValue = keys[i + 1];

            int currentDepth = getDepth(currentValue);
            if ((xorKey ^ currentValue) == key) {
                if (currentDepth > depth && flag != TTResult.EXACT_VALUE) {
                    noReplaceCacheAlreadyBetter++;
                    return;
                }
                replaceIndex = i;
                break;
            }

            // replace the lowest depth
            if (currentDepth < replacedDepth) {
                replaceIndex = i;
                replacedDepth = currentDepth;
            }
        }

        final long value = createValue(score, move, flag, depth);

        keys[replaceIndex] = key ^ value;
        keys[replaceIndex + 1] = value;
    }

    public static int getScore(final long value) {
        return (int) (value >> SCORE);
    }

    public int getDepth(final long value) {
        return (int) ((value & 0x3ff) - halfMoveCounter);
    }

    public static int getFlag(final long value) {
        return (int) (value >>> FLAG & 3);
    }

    public static int getMove(final long value) {
        return (int) (value >>> MOVE & 0xffffffff);
    }

    // SCORE,HALF_MOVE_COUNTER,MOVE,FLAG,DEPTH
    public long createValue(final long score, final long move, final long flag, final int depth) {
        return score << SCORE | move << MOVE | flag << FLAG | (depth + halfMoveCounter);
    }

    public String toString(long ttValue) {
        return "score=" + getScore(ttValue) + /*" " + new MoveWrapper(getMove(ttValue)) +*/ " depth=" + getDepth(
                ttValue)
                + " flag="
                + getFlag(ttValue);
    }

    /**
     * Gives a raw statistical usage by inspecting the first 1000 entries.
     * If the cache is well distributing the values this gives a good match of the overal usage.
     *
     * @return
     */
    public long getUsagePercentage() {
        int usage = 0;
        for (int i = 0; i < 2000; i += 2) {
            if (keys[i] != 0) {
                usage++;
            }
        }
        return usage;
    }

    /**
     * Calculates the real usage of the cache inspecting all entries. Since this takes more time, this is only
     * used in test cases to inspect real cache usage.
     *
     * @return
     */
    public long getPreciseUsagePercentage() {
        int usage = 0;
        for (int i = 0; i < keys.length; i += 2) {
            if (keys[i] != 0) {
                usage++;
            }
        }
        return (usage * 1000 / (keys.length / 2));

    }

    /**
     * Calculates a heuristic about the overal usage distribution of the cache.
     * Used in testcases to verify proper distribution of cache values.
     *
     * @return
     */
    public int[] fillHeuristic() {

        int chunks = 100;
        int[] heuristics = new int[chunks];
        int chunkSize = keys.length / chunks;

        for (int chunk = 0; chunk < chunks; chunk++) {
            int from = chunk * chunkSize;
            int to = from + chunkSize;
            heuristics[chunk] = calcChunkHeuristic(from, to, chunkSize);
        }
        return heuristics;
    }

    private int calcChunkHeuristic(int from, int to, int chunkSize) {
        int usage = 0;
        for (int i = from; i < to; i += 2) {
            if (keys[i] != 0) {
                usage++;
            }
        }
        return (usage * 1000 / (chunkSize / 2));
    }

    public boolean findEntry(TTResult result, BoardRepresentation board) {
        long v = getValue(board.getZobristHash());
        if (v != NORESULT) {
            result.setDepth(getDepth(v));
            result.setType((byte) getFlag(v));
            result.setScore(getScore(v));
            result.setMove(getMove(v));
            return true;
        }
        return false;
    }

    public int findHashMove(BoardRepresentation board) {
        long v = getValue(board.getZobristHash());
        return v != NORESULT ? getMove(v) : 0;
    }

    public void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta, int depth,
            int move) {
        addValue(currBoard.getZobristHash(), max, depth, toFlag(max, alpha, beta), move);
    }

    public void updateAging(BoardRepresentation board) {
        LOGGER.info("hits: " + cacheHits + "; fails:" + cacheMisses);
        halfMoveCounter = aging.updateAging(board);
        //		if (halfMoveCounter>512){
        //			halfMoveCounter=0;
        //		}
    }

    public void checkUpdateCacheSize() {
        if (getConfiguredMbSize() != mbSize) {
            initCache();
        }
    }

    public long calcHashFull() {
        return getUsagePercentage();
    }

    public int getCacheSize() {
        return keys.length * 8;
    }

    public void resetStatistics() {
        cacheHits = 0;
        cacheMisses = 0;
        noReplaceCacheAlreadyBetter = 0;
    }
}
