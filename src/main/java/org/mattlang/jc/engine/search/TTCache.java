package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.TTEntry.TTType.*;

import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTCache implements StatisticsCollector {

    public static final int CAPACITY = 10_000_000;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    private TTEntry[] whitemap = new TTEntry[CAPACITY];
    private TTEntry[] blackmap = new TTEntry[CAPACITY];

    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();
        int hashEntry = (int) Math.floorMod(boardZobristHash, CAPACITY);
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
        if (entry.zobristHash == boardZobristHash) {
            cacheHit++;
            return entry;
        } else {
            colission++;
            cacheFail++;
            return null;
        }
    }

    public final void storeTTEntry(BoardRepresentation board, Color side, int eval, TTEntry.TTType tpe, int depth) {
        // only store entries with lower depth:
        TTEntry existing = getTTEntry(board, side);
        if (existing == null || existing.depth > depth) {
            long boardZobristHash = board.getZobristHash();
            storeTT(boardZobristHash, side, new TTEntry(boardZobristHash, eval, tpe, depth));
        }
    }

    private void storeTT(long boardZobristHash, Color side, TTEntry ttEntry) {
        int hashEntry = (int) Math.floorMod(boardZobristHash, CAPACITY);
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
            int depth) {
        if (max <= alpha) // a lowerbound value
            storeTTEntry(currBoard, color, max, LOWERBOUND, depth);
        else if (max >= beta) // an upperbound value
            storeTTEntry(currBoard, color, max, UPPERBOUND, depth);
        else // a true minimax value
            storeTTEntry(currBoard, color, max, EXACT_VALUE, depth);

    }

    @Override
    public void resetStatistics() {
        cacheHit = 0;
        cacheFail = 0;
    }

    @Override
    public void collectStatistics(Map stats) {
        stats.put("size", size);
        stats.put("cacheHit", cacheHit);
        stats.put("cacheFail", cacheFail);
        stats.put("colissions", colission);
        if (cacheHit + cacheFail != 0) {
            stats.put("hit/all", cacheHit * 100 / (cacheHit + cacheFail) + "%");
        }
    }
}
