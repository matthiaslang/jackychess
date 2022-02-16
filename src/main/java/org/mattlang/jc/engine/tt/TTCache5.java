package org.mattlang.jc.engine.tt;

import static java.lang.String.format;
import static org.mattlang.jc.engine.tt.LongCache.toFlag;
import static org.mattlang.jc.engine.tt.TTEntry.EXACT_VALUE;

import java.util.Map;

import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

/**
 * Cache combining two replacement strategies: "by depth" and "always replace" and aging.
 * It uses therefore two maps, one for each strategy.
 *
 * The effect should be the following:
 *
 * An entry to save will at least always be saved, no matter of any criterias. So latest entries will always be kept in
 * the cache. This should be good when searching near the leafs.
 *
 * On the other side, entries with highest depth will also be kept in the cache. This is of course necessary for
 * pruning on higher depth.
 */
public final class TTCache5 implements TTCacheInterface {

    public static final int BIT_SIZE = 23;

    public static final int CAPACITY = 1 << BIT_SIZE;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    /**
     * map to replace by Depth.
     */
    private TTE[] mapD = new TTE[CAPACITY];

    /**
     * map using the "always replace" strategy.
     */
    private TTE[] mapA = new TTE[CAPACITY];

    private final TTEntry leightweightEntry = new TTEntry(0L, 0, (byte) 0, 0, (byte) 0, 0);

    @Override
    public boolean findEntry(TTResult result, BoardRepresentation board) {
        TTEntry entry = getTTEntry(board, null);
        if (entry != null) {
            result.setDepth(entry.getDepth());
            result.setType(entry.getType());
            result.setScore(entry.getValue());
            result.setMove(entry.getMove());
            return true;
        }
        return false;
    }

    @Override
    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = h0(boardZobristHash);

        TTE tte = mapD[hashEntry];

        if (tte != null && tte.zobristHash == boardZobristHash /*&& tte.depth -currAging>=depth*/) {
            cacheHit++;
            leightweightEntry.updateRetVals(tte.getValue(), tte.getType(), tte.getDepth() - currAging, tte.getMove());
            return leightweightEntry;
        }

        tte = mapA[hashEntry];
        if (tte != null && tte.zobristHash == boardZobristHash /*&& tte.depth -currAging>=depth*/) {
            cacheHit++;
            leightweightEntry.updateRetVals(tte.getValue(), tte.getType(), tte.getDepth() - currAging, tte.getMove());
            return leightweightEntry;
        }
        cacheFail++;
        return null;

    }

    private final void storeTTEntry(BoardRepresentation board, int eval, byte tpe, int depth, int move) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = h0(boardZobristHash);

        TTE tteD = mapD[hashEntry];
        if (tteD == null) {
            mapD[hashEntry] = new TTE(boardZobristHash, eval, tpe, depth + currAging, move);
            size++;
        } else if (tteD.zobristHash == boardZobristHash && tteD.depth - currAging > depth) {
            // do not replace lower depth values for the same zobrist key
            return;
        } else if (move != 0 && (tteD.depth - currAging < depth
                || tpe == EXACT_VALUE && tteD.getType() != EXACT_VALUE)) {
            // replace if depth is higher or an exact value would replace an unexact value;
            // we do not want to replace entries with no move info here (this is connected to our quiescence tt entry saveing which is anyway a bit weird and should be removed later...)

            // try to save an existing in the "always replace table" if the entry is free:
            if (boardZobristHash != tteD.zobristHash && mapA[hashEntry] == null) {
                mapA[hashEntry] = mapD[hashEntry];
                mapD[hashEntry] = new TTE(boardZobristHash, eval, tpe, depth + currAging, move);
            } else {
                tteD.update(boardZobristHash, eval, tpe, depth + currAging, move);
            }
        } else {
            // use the always replace strategy:
            TTE tteA = mapA[hashEntry];
            if (tteA == null) {
                mapA[hashEntry] = new TTE(boardZobristHash, eval, tpe, depth + currAging, move);
                size++;
            } else if (tteA.zobristHash == boardZobristHash && tteA.depth - currAging > depth) {
                return;
            } else {
                tteA.update(boardZobristHash, eval, tpe, depth + currAging, move);
            }
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
        stats.put("size", size);
        stats.put("cacheQueries", cacheHit + cacheFail);
        stats.put("cacheHit", cacheHit);
        stats.put("cacheFail", cacheFail);
        stats.put("colissions", colission);
        if (cacheHit + cacheFail != 0) {
            stats.put("hit/all", (long) (cacheHit) * 100 / (cacheHit + cacheFail) + "%");
        }
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    public void updateAging(BoardRepresentation board) {
        currAging = aging.updateAging(board);

        //        currAging++;
        //        if (currAging>128){
        //            currAging=1;
        //        }
        int hitPercent = 0;
        if (cacheHit + cacheFail != 0) {
            hitPercent = cacheHit * 100 / (cacheHit + cacheFail);
        }
        UCILogger.log(format("TTCache: size: %s hits: %s fails: %s %s pct collisions: %s",
                size, cacheHit, cacheFail, hitPercent, colission));

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
        int usage = 0;
        for (int i = 0; i < 1000; i++) {
            if (mapD[i] != null) {
                usage++;
            }
        }
        return usage;
    }
}
