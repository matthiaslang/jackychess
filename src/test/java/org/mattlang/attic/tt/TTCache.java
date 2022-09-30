package org.mattlang.attic.tt;

import static org.mattlang.jc.Constants.DEFAULT_CACHE_SIZE_MB;
import static org.mattlang.jc.engine.tt.TTResult.toFlag;

import java.util.Map;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.tt.TTAging;
import org.mattlang.jc.engine.tt.TTCacheInterface;
import org.mattlang.jc.engine.tt.TTResult;

public final class TTCache implements TTCacheInterface {

    private static final Logger LOGGER = Logger.getLogger(TTCache.class.getSimpleName());

    /**
     * Slotsize is 16 for two long values
     */
    public static final int SLOT_SIZE = 8 * 2;

    private int cacheHit;
    private int cacheFail;
    private int colission;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    private LongCache cache = new LongCache(determineBitSizeFromConfig());

    private int mbSize = DEFAULT_CACHE_SIZE_MB;

    private int determineBitSizeFromConfig() {
        int mb = getConfiguredMbSize();
        mbSize=mb;
        return determineCacheBitSizeFromMb(mb, SLOT_SIZE);
    }

    private int getConfiguredMbSize() {
        Integer mb = Factory.getDefaults().getConfig().hash.getValue();

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

    @Override
    public boolean findEntry(TTResult result, BoardRepresentation board) {
        long entry = cache.find(board.getZobristHash());
        if (entry != LongCache.NORESULT) {
            result.setDepth(LongCache.getDepth(entry));
            result.setType(LongCache.getFlag(entry));
            result.setScore(LongCache.getScore(entry));
            result.setMove(LongCache.getMove(entry));
            return true;
        }
        return false;
    }

    private final void storeTTEntry(BoardRepresentation board, int eval, byte tpe, int depth, int move) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        long existing = cache.getSlotValue(boardZobristHash);
        int existingDepth = LongCache.getDepth(existing);

        if (existing == 0L) {
            cache.save(boardZobristHash, LongCache.createValue(eval, move, tpe, depth));
        } else if (existingDepth < depth) {
            cache.save(boardZobristHash, LongCache.createValue(eval, move, tpe, depth));
        }
    }

    @Override
    public final void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta,
            int depth, int move) {
            storeTTEntry(currBoard, max, toFlag(max, alpha, beta), depth, move);
    }

    @Override
    public void resetStatistics() {
        cacheHit = 0;
        cacheFail = 0;
        colission = 0;
    }

    @Override
    public void collectStatistics(Map stats) {
        //        stats.put("size", size);
        stats.put("cacheQueries", cacheHit + cacheFail);
        stats.put("cacheHit", cacheHit);
        stats.put("cacheFail", cacheFail);
        stats.put("colissions", colission);
        if (cacheHit + cacheFail != 0) {
            stats.put("hit/all", (long) (cacheHit) * 100 / (cacheHit + cacheFail) + "%");
        }
    }

    public void updateAging(BoardRepresentation board) {
        checkUpdateCacheSize();
        //        currAging = aging.updateAging(board);

        //        int hitPercent = 0;
        //        if (cacheHit + cacheFail != 0) {
        //            hitPercent = cacheHit * 100 / (cacheHit + cacheFail);
        //        }
        //        UCILogger.log(format("TTCache: size: %s hits: %s fails: %s %s pct collisions: %s",
        //                size, cacheHit, cacheFail, hitPercent, colission));

    }

    private void checkUpdateCacheSize() {
        if (getConfiguredMbSize() != mbSize) {
            cache = new LongCache(determineBitSizeFromConfig());
        }
    }

    /**
     * calc hashfull permill
     *
     * @return
     */
    public long calcHashFull() {
        return getUsagePercentage();
    }

    public long getUsagePercentage() {
        return cache.getUsagePercentage();
    }

    @Override
    public boolean isUsableForLazySmp() {
        return true;
    }

    @Override
    public void reset() {
        cache.reset();
    }
}
