package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.engine.tt.LongCache.toFlag;

import java.util.Map;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public final class TTCache implements TTCacheInterface {

    private int cacheHit;
    private int cacheFail;
    private int colission;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    private LongCache cache = new LongCache(23);

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

    @Override
    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        throw new IllegalStateException("unsupported!");
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
        //        currAging = aging.updateAging(board);

        //        int hitPercent = 0;
        //        if (cacheHit + cacheFail != 0) {
        //            hitPercent = cacheHit * 100 / (cacheHit + cacheFail);
        //        }
        //        UCILogger.log(format("TTCache: size: %s hits: %s fails: %s %s pct collisions: %s",
        //                size, cacheHit, cacheFail, hitPercent, colission));

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
}
