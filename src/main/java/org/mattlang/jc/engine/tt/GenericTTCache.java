package org.mattlang.jc.engine.tt;

import static java.lang.String.format;

import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class GenericTTCache<T> implements StatisticsCollector {

    public static final int bitSize = 21;

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

    private long[] whiteZobrist = new long[CAPACITY];
    private long[] blackZobrist = new long[CAPACITY];
    private T[] whitemap = (T[]) new Object[CAPACITY];
    private T[] blackmap = (T[]) new Object[CAPACITY];

    public final T getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = h0(boardZobristHash);
        switch (side) {
        case WHITE:
            return checkFoundEntry(whitemap[hashEntry], whiteZobrist[hashEntry], boardZobristHash);
        case BLACK:
            return checkFoundEntry(blackmap[hashEntry], blackZobrist[hashEntry], boardZobristHash);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    private T checkFoundEntry(T entry, long entryZobristHash, long boardZobristHash) {
        if (entry == null) {
            cacheFail++;
            return null;
        }
        if (entryZobristHash == boardZobristHash) {
            cacheHit++;
            return entry;
        } else {
            colission++;
            cacheFail++;
            return null;
        }
    }

    public final void storeTTEntry(BoardRepresentation board, Color side, T t) {
        long boardZobristHash = board.getZobristHash();

        storeTT(boardZobristHash, side, t);

    }

    private void storeTT(long boardZobristHash, Color side, T ttEntry) {
        int hashEntry = h0(boardZobristHash);
        switch (side) {
        case WHITE:
            if (whitemap[hashEntry] == null) {
                size++;
            }
            whitemap[hashEntry] = ttEntry;
            whiteZobrist[hashEntry] = boardZobristHash;
            break;
        case BLACK:
            if (blackmap[hashEntry] == null) {
                size++;
            }
            blackmap[hashEntry] = ttEntry;
            blackZobrist[hashEntry] = boardZobristHash;
            break;
        }
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

}
