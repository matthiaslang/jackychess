package org.mattlang.jc.engine.tt;

import static java.lang.String.format;
import static org.mattlang.jc.engine.tt.TTEntry.*;

import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTCache implements StatisticsCollector {

    public static final int bitSize = 23;

    public static final int CAPACITY = 1 << bitSize;
    public static final int MAX_AGE = 2;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    private TTEntry[] whitemap = new TTEntry[CAPACITY];
    private TTEntry[] blackmap = new TTEntry[CAPACITY];

    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = h0(boardZobristHash);
        switch (side) {
        case WHITE:
            return checkFoundEntry(whitemap[hashEntry], boardZobristHash);
        case BLACK:
            return checkFoundEntry(blackmap[hashEntry], boardZobristHash);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    private TTEntry checkFoundEntry(TTEntry entry, long boardZobristHash) {
        if (entry == null) {
            cacheFail++;
            return null;
        }
        if (!entry.isEmpty() && entry.zobristHash == boardZobristHash && withinAge(entry)) {
            cacheHit++;
            return entry;
        } else {
            colission++;
            cacheFail++;
            return null;
        }
    }

    private boolean withinAge(TTEntry entry) {
        return currAging - entry.getAging() < MAX_AGE;
    }

    public final void storeTTEntry(BoardRepresentation board, Color side, int eval, byte tpe, int depth, int move) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        TTEntry existing = getTTEntry(board, side);
        if (existing == null) {
            storeTT(boardZobristHash, side, new TTEntry(boardZobristHash, eval, tpe, depth, currAging, move));
        } else if (existing.isEmpty() || existing.depth > depth || existing.getAging() != currAging) {
            // todo we should not use the aging criteria (existing.getAging() != currAging) probably... as a new entry should be prefered always when saving
            existing.update(boardZobristHash, eval, tpe, depth, currAging, move);
        }
    }

    private void storeTT(long boardZobristHash, Color side, TTEntry ttEntry) {
        int hashEntry = h0(boardZobristHash);
        switch (side) {
        case WHITE:
            if (whitemap[hashEntry] == null) {
                size++;
            }
            whitemap[hashEntry] = ttEntry;
            break;
        case BLACK:
            if (blackmap[hashEntry] == null) {
                size++;
            }
            blackmap[hashEntry] = ttEntry;
            break;
        }
    }

    public final void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta,
            int depth, int move) {
        if (max <= alpha) // a lowerbound value
            storeTTEntry(currBoard, color, max, LOWERBOUND, depth, move);
        else if (max >= beta) // an upperbound value
            storeTTEntry(currBoard, color, max, UPPERBOUND, depth, move);
        else // a true minimax value
            storeTTEntry(currBoard, color, max, EXACT_VALUE, depth, move);

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
            stats.put("hit/all", cacheHit * 100 / (cacheHit + cacheFail) + "%");
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
        return (long)size *1000 / (CAPACITY * 2);
    }
}
