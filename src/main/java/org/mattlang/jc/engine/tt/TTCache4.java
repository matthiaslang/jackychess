package org.mattlang.jc.engine.tt;

import static java.lang.String.format;
import static org.mattlang.jc.engine.tt.TTEntry.*;

import java.util.Map;

import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public final class TTCache4 implements TTCacheInterface {

    public static final int bitSize = 25;

    public static final int CAPACITY = 1 << bitSize;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    private TTE[] map = new TTE[CAPACITY];

    private final TTEntry leightweightEntry = new TTEntry(0L, 0, (byte) 0, 0, (byte) 0, 0);

    @Override
    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        TTE tte = getInternalTTEntry(board);
        if (tte == null) {
            return null;
        }
        leightweightEntry.updateRetVals(tte.getValue(), tte.getType(), tte.getDepth() - currAging, tte.getMove());
        return leightweightEntry;
    }

    public final TTE getInternalTTEntry(BoardRepresentation board) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = h0(boardZobristHash);
        return checkFoundEntry(map[hashEntry], boardZobristHash);
    }

    private TTE checkFoundEntry(TTE entry, long boardZobristHash) {
        if (entry == null) {
            cacheFail++;
            return null;
        }
        if (entry.zobristHash == boardZobristHash) {
            cacheHit++;
            return entry;
        } else {
            colission++;
            cacheFail++;
            return null;
        }
    }

    private final void storeTTEntry(BoardRepresentation board, int eval, byte tpe, int depth, int move) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        TTE existing = getInternalTTEntry(board);
        if (existing == null) {
            storeTT(boardZobristHash, new TTE(boardZobristHash, eval, tpe, depth + currAging, move));
        } else if (existing.depth - currAging > depth) {
            // todo we should not use the aging criteria (existing.getAging() != currAging) probably... as a new entry should be prefered always when saving
            existing.update(boardZobristHash, eval, tpe, depth + currAging, move);
        }
    }

    private void storeTT(long boardZobristHash, TTE ttEntry) {
        int hashEntry = h0(boardZobristHash);
        if (map[hashEntry] == null) {
            size++;
        }
        map[hashEntry] = ttEntry;

    }

    @Override
    public final void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta,
            int depth, int move) {
        if (max <= alpha) // a lowerbound value
            storeTTEntry(currBoard, max, LOWERBOUND, depth, move);
        else if (max >= beta) // an upperbound value
            storeTTEntry(currBoard, max, UPPERBOUND, depth, move);
        else // a true minimax value
            storeTTEntry(currBoard, max, EXACT_VALUE, depth, move);

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
            stats.put("hit/all", (long)(cacheHit) * 100 / (cacheHit + cacheFail) + "%");
        }
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    private final int h1(long key) {
        return (int) ((key >> 32) & (CAPACITY - 1));
    }

    public void updateAging(BoardRepresentation board) {
        currAging = aging.updateAging(board);

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
            if (map[i] != null) {
                usage++;
            }
        }
        return usage;
    }
}
