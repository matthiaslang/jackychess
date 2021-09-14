package org.mattlang.jc.engine.tt;

import static java.lang.String.format;
import static org.mattlang.jc.engine.tt.TTEntry.*;

import java.util.Map;

import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTCache2 implements TTCacheInterface {

    public static final int bitSize = 23;

    public static final int CAPACITY = 1 << bitSize;
    public static final int MAX_AGE = 2;

    public static final int BUCKETS = 5;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;

    private TTAging aging = new TTAging();

    private TTEntry[] whitemap = new TTEntry[CAPACITY + BUCKETS];
    private TTEntry[] blackmap = new TTEntry[CAPACITY + BUCKETS];

    @Override
    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();
        int index = h0(boardZobristHash);
        return checkFoundEntry(getMap(side), index, boardZobristHash);
    }

    private TTEntry checkFoundEntry(TTEntry[] map, int index, long boardZobristHash) {
        for (int idx = index; idx < index + BUCKETS; idx++) {
            TTEntry entry = map[idx];
            if (entry != null && entry.zobristHash == boardZobristHash /*&& withinAge(entry)*/) {
                cacheHit++;
                return entry;
            }
        }
        cacheFail++;
        return null;
    }

    private boolean withinAge(TTEntry entry) {
        return currAging - entry.getAging() < MAX_AGE;
    }

    private TTEntry checkFoundEntryForStoring(TTEntry[] map, int index, long boardZobristHash, int depth, byte tpe) {
        // try for a a direct zobrist match or a free entry:
        int idxOfEntryWithHigherDepth = -1;
        int idxOfEntryOutdated = -1;
        int idxOfEntryWithLowerType = -1;

        for (int idx = index; idx < index + BUCKETS; idx++) {
            TTEntry entry = map[idx];
            if (entry == null) {
                entry = new TTEntry(boardZobristHash, 0, (byte) 0, 0, (byte) 0, 0);
                map[idx] = entry;
                return entry;
            } else if (entry.zobristHash == boardZobristHash) {
                return entry;
            } else if (!withinAge(entry)) {
                idxOfEntryOutdated = idx;
            } else if (entry.depth > depth) {
                idxOfEntryWithHigherDepth = idx;
            } else if (entry.isLower(tpe)) {
                idxOfEntryWithLowerType=idx;
            }
        }
        // if all buckets are filled and no one with the direct zobrist hash, we need to exchange one of them with an older age:
        if (idxOfEntryOutdated >= 0) {
            return map[idxOfEntryOutdated];
        }

        // otherwise choose one with a lower prioritized type:
        if (idxOfEntryWithLowerType >=0){
            return map[idxOfEntryWithLowerType];
        }

        // otherwise choose the one with a higher depth:
        if (idxOfEntryWithHigherDepth >= 0) {
            return map[idxOfEntryWithHigherDepth];
        }

        // otherwise, we have no other way than just delivering the last bucket for saving:
        return map[index + BUCKETS - 1];

    }

    private final TTEntry findTTEntryForStoring(BoardRepresentation board, Color side, int depth, byte tpe) {
        long boardZobristHash = board.getZobristHash();
        int index = h0(boardZobristHash);
        return checkFoundEntryForStoring(getMap(side), index, boardZobristHash, depth, tpe);
    }

    private final TTEntry[] getMap(Color side) {
        return side == Color.WHITE ? whitemap : blackmap;
    }

    private final void storeTTEntry(BoardRepresentation board, Color side, int eval, byte tpe, int depth, int move) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        TTEntry entry = findTTEntryForStoring(board, side, depth, tpe);
        if (entry == null) {
            // we have no place to store it
        } else if (entry.depth > depth || entry.getAging() != currAging) {
            entry.update(boardZobristHash, eval, tpe, depth, currAging, move);
        }
    }

    @Override
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
            stats.put("hit/all", (long) (cacheHit) * 100 / (cacheHit + cacheFail) + "%");
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
        return (long) size * 1000 / (CAPACITY * 2);
    }
}
