package org.mattlang.jc.engine.tt;

import static java.util.logging.Level.INFO;
import static org.mattlang.jc.Constants.DEFAULT_CACHE_SIZE_MB;
import static org.mattlang.jc.engine.tt.TTResult.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.board.BoardRepresentation;

import lombok.Getter;

/**
 * Cache using only a long array to be faster and more memory efficient.
 * It is also thread-safe (thread consistent for read access), so that it could be used for a Lazy-SMP Search algorithm.
 * It combines a "always save" with replace "lowest depth" in a multi bucket cache.
 */
public final class TTCache {

    public static final long NORESULT = Long.MAX_VALUE;

    private static final Logger LOGGER = Logger.getLogger(TTCache.class.getSimpleName());

    /**
     * number of buckets used for one index entry.
     */
    private static final int BUCKET_SIZE = 3;

    /* slot size: number of longs for one slot. */
    private static final int SLOT_SIZE = 2;

    /**
     * size of the buckets of one "index": the number of buckets mult the size of one slot.
     */
    private static final int BUCKET_CHUNK_SIZE = BUCKET_SIZE * SLOT_SIZE;

    private static final int BYTE_SIZE_SLOT = 8 * SLOT_SIZE;
    public static final long MEGABYTE = 1024 * 1024;

    private static final long STATIC_EVAL_MASK = 0xffff000000000000L;
    //    private static final long AGE_MASK            = 0x0000ffff00000000L;
    private static final long ZOBRIST_PART_MASK = 0x0000ffffffffffffL;

    // key, value
    private long[] keys;

    private final TTAging aging = new TTAging();

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

    private int determineBitSizeFromConfig() {
        int mb = getConfiguredMbSize();
        mbSize = mb;
        return determineCacheBitSizeFromMb(mb, BYTE_SIZE_SLOT);
    }

    private int getConfiguredMbSize() {
        Integer mb = ConfigValues.getConfigValues().hash.getValue();

        if (mb == null) {
            return DEFAULT_CACHE_SIZE_MB;
        }
        return mb.intValue();
    }

    public static int determineCacheBitSizeFromMb(int mb, int sizeOfSlot) {
        long slots = mb * MEGABYTE / sizeOfSlot;
        int bits = (int) (Math.log(slots) / Math.log(2));
        LOGGER.info("cache of " + mb + "MB: setting cache to " + slots + " slots, " + bits + " bits");
        return bits;
    }

    public TTCache() {
        initCache();
    }

    private void initCache() {
        int bitSize = determineBitSizeFromConfig();
        final int maxEntries = (int) (1L << bitSize) * SLOT_SIZE;
        LOGGER.info("TT Cache: allocating " + maxEntries + " longs;");

        keys = new long[maxEntries];
        // entries is size div bucket size div data size.
        indexPlaces = maxEntries / BUCKET_CHUNK_SIZE;
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
        final int index = findIndex(key);
        if (index == -1) {
            return NORESULT;
        }
        return keys[index + 1];
    }

    public int findIndex(final long key) {
        final int index = getIndex(key);
        final long partialKey = partialKey(key);
        for (int i = index; i < index + BUCKET_CHUNK_SIZE; i += SLOT_SIZE) {
            long xorKey = keys[i];
            long value = keys[i + 1];
            if (partialKey(xorKey ^ value) == partialKey) {
                if (BuildConstants.STATS_ACTIVATED) {
                    cacheHits++;
                }
                return i;
            }
        }

        cacheMisses++;
        return -1;
    }

    /**
     * Calculates the index place in the data.
     *
     * @param key
     * @return
     */
    private int getIndex(final long key) {
        // xor upper and lower halves of zobrist together and apply mask to have positive values:
        long index = (key ^ (key >>> 32)) & 0x7FFFFFFF;
        // use modulo to ensure better distribution of the values over the array.
        return (int) (index % indexPlaces) * BUCKET_CHUNK_SIZE;
    }

    public void addValue(final long key, int score, final int depth, final int flag, final int move, int eval) {
        final long partialKey = partialKey(key);
        final int index = getIndex(key);
        long replacedDepth = Integer.MAX_VALUE;
        int replaceIndex = index;
        for (int i = index; i < index + BUCKET_CHUNK_SIZE; i += SLOT_SIZE) {

            long xorKey = keys[i];
            if (xorKey == 0) {
                replaceIndex = i;
                if (BuildConstants.STATS_ACTIVATED) {
                    numFilledEntries++;
                }
                break;
            }

            long currentValue = keys[i + 1];
            int currentDepth = getDepth(currentValue);
            if (partialKey(xorKey ^ currentValue) == partialKey) {
                if (currentDepth > depth && flag != TTResult.EXACT_VALUE) {
                    if (BuildConstants.STATS_ACTIVATED) {
                        noReplaceCacheAlreadyBetter++;
                    }
                    // at least save an eval if we have one:
                    if (flag == ONLY_EVAL) {
                        keys[replaceIndex] = createKeyContent(key, eval) ^ currentValue;
                    }
                    return;
                }
                replaceIndex = i;
                // preserve existing evals if we do not have a new one on direct key matches:
                int savedEval = getStaticEval(xorKey ^ currentValue);
                if (savedEval != NO_HASH_EVAL && eval == NO_HASH_EVAL) {
                    eval = savedEval;
                }
                break;
            }

            // replace the lowest depth
            if (currentDepth < replacedDepth) {
                replaceIndex = i;
                replacedDepth = currentDepth;
            }
        }

        final long value = createValue(score, move, flag, depth);

        keys[replaceIndex] = createKeyContent(key, eval) ^ value;
        keys[replaceIndex + 1] = value;
    }

    private static long partialKey(long key) {
        return key & ZOBRIST_PART_MASK;
    }

    private static long createKeyContent(long key, int staticEval) {
        return (key & ZOBRIST_PART_MASK) | ((long) (staticEval & 0xFFFF) << 48);
    }

    public static int getStaticEval(long key) {
        return (short) ((key & STATIC_EVAL_MASK) >>> 48);
    }

    public void addValueByTTIndex(final long key, final int replaceIndex, int score, final int depth, final int flag,
            final int move, int eval) {
        long currentValue = keys[replaceIndex + 1];
        int currentDepth = getDepth(currentValue);

        if (currentDepth > depth && flag != TTResult.EXACT_VALUE) {
            if (BuildConstants.STATS_ACTIVATED) {
                noReplaceCacheAlreadyBetter++;
            }
            // at least save an eval if we have one:
            if (flag == ONLY_EVAL) {
                keys[replaceIndex] = createKeyContent(key, eval) ^ currentValue;
            }
            return;
        }

        // preserve existing evals if we do not have a new one on direct key matches:
        long xorKey = keys[replaceIndex];
        int savedEval = getStaticEval(xorKey ^ currentValue);
        if (savedEval != NO_HASH_EVAL && eval == NO_HASH_EVAL) {
            eval = savedEval;
        }

        final long value = createValue(score, move, flag, depth);

        keys[replaceIndex] = createKeyContent(key, eval) ^ value;
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
     * If the cache is well distributing the values this gives a good match of the overall usage.
     * <p>
     * It counts only "empty" slots and identifies "very old slots", so the statistic is not completely up to date, but
     * good enough to give a hint on the cache usage.
     *
     * @return
     */
    public long getUsagePercentage() {
        int usage = 0;
        for (int i = 0; i < 2000; i += SLOT_SIZE) {
            long value = keys[i + 1];
            // clean up very old keys, to keep the usage statistic more up to date
            if (getDepth(value) < 1) {
                keys[i] = 0L;
            }
            if (keys[i] != 0) {
                usage++;
            }

        }
        return usage;
    }

    private boolean isEffectivelyUsedEntry(int i) {
        if (keys[i] == 0) {
            return false;
        }
        long value = keys[i + 1];
        return getDepth(value) > 0;
    }

    /**
     * Calculates the real usage of the cache inspecting all entries. Since this takes more time, this is only
     * used in test cases to inspect real cache usage.
     *
     * @return
     */
    public long getPreciseUsagePercentage() {
        int usage = 0;
        for (int i = 0; i < keys.length; i += SLOT_SIZE) {
            if (keys[i] != 0) {
                usage++;
            }
        }
        return (usage * 1000 / (keys.length / SLOT_SIZE));

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
        for (int i = from; i < to; i += SLOT_SIZE) {
            if (keys[i] != 0) {
                usage++;
            }
        }
        return (usage * 1000 / (chunkSize / SLOT_SIZE));
    }

    public boolean findEntry(TTResult result, BoardRepresentation board) {
        int index = findIndex(board.getZobristHash());
        if (index != -1) {
            long xorKey = keys[index];
            long v = keys[index + 1];
            result.setDepth(getDepth(v));
            result.setType((byte) getFlag(v));
            result.setScore(getScore(v));
            result.setMove(getMove(v));
            result.setEval(getStaticEval(xorKey ^ v));
            result.setIndex(index);
            return true;
        }
        return false;
    }

    public int findHashMove(BoardRepresentation board) {
        long v = getValue(board.getZobristHash());
        return v != NORESULT ? getMove(v) : 0;
    }

    public void storeTTEntry(BoardRepresentation currBoard, int ttIndex, int max, int alpha, int beta, int depth,
            int move, int eval) {
        //        if (ttIndex != -1) {
        //            addValueByTTIndex(currBoard.getZobristHash(), ttIndex, max, depth, toFlag(max, alpha, beta), move, eval);
        //        } else {
        addValue(currBoard.getZobristHash(), max, depth, toFlag(max, alpha, beta), move, eval);
        //        }
    }

    public void storeTTEntry(BoardRepresentation currBoard, int ttIndex, int depth,
            int eval) {
        addValue(currBoard.getZobristHash(), 0, depth, ONLY_EVAL, 0, eval);
    }

    public void updateAging(BoardRepresentation board) {
        if (LOGGER.isLoggable(INFO)) {
            LOGGER.info("hits: " + cacheHits + "; fails:" + cacheMisses);
        }
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

    public long getCacheSize() {
        return keys.length * 8L;
    }

    public void resetStatistics() {
        cacheHits = 0;
        cacheMisses = 0;
        noReplaceCacheAlreadyBetter = 0;
    }
}
