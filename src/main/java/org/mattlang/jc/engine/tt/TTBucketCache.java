package org.mattlang.jc.engine.tt;

import static java.lang.String.format;
import static org.mattlang.jc.engine.tt.TTEntry.*;

import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class TTBucketCache implements StatisticsCollector {

    public static final int bitSize = 23;

    public static final int CAPACITY = 1 << bitSize;

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int fullBuckets;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;
    /**
     * the last board representation to check for aging.
     */
    private BoardRepresentation lastBoard;

    private TTBucket[] whitemap = new TTBucket[CAPACITY];
    private TTBucket[] blackmap = new TTBucket[CAPACITY];

    public TTBucketCache() {

    }

    public final TTEntry getTTEntry(BoardRepresentation board, Color side) {
        long boardZobristHash = board.getZobristHash();

        switch (side) {
        case WHITE:
            return search(whitemap, boardZobristHash);
        case BLACK:
            return search(blackmap, boardZobristHash);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    /**
     * Searches for a fee node which can be used to save a new entry.
     *
     * @param boardZobristHash
     * @param side
     * @return
     */
    private final TTEntry findFreeTTEntry(long boardZobristHash, int depth, Color side) {
        switch (side) {
        case WHITE:
            return findFreeTTEntry(whitemap, boardZobristHash, depth);
        case BLACK:
            return findFreeTTEntry(blackmap, boardZobristHash, depth);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    /**
     * Find a free entry to save information by checking the h0 and h1 index of the table.
     * Either a empty node, or a aged node
     * or a node with higher depth.
     * null if no free node could be found.
     *
     * @param map
     * @param boardZobristHash
     * @param depth
     * @return
     */
    private TTEntry findFreeTTEntry(TTBucket[] map, long boardZobristHash, int depth) {
        int hash = h0(boardZobristHash);
        TTBucket bucket = map[hash];
        if (bucket == null) {
            bucket = new TTBucket();
            map[hash] = bucket;
        }
        TTEntry entry = bucket.findFreeEntry(depth, boardZobristHash, currAging);
        return entry;
    }

    private TTEntry search(TTBucket[] map, long boardZobristHash) {
        TTBucket bucket = map[h0(boardZobristHash)];
        if (bucket == null) {
            cacheFail++;
            return null;
        }
        TTEntry entry = bucket.search(boardZobristHash, currAging);
        if (entry == null) {
            cacheFail++;
        } else {
            cacheHit++;
        }
        return entry;
    }

    private final void storeTTEntry(BoardRepresentation board, Color side, int eval, byte tpe, int depth) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        TTEntry freeOne = findFreeTTEntry(boardZobristHash, depth, side);
        if (freeOne != null) {
            freeOne.update(boardZobristHash, eval, tpe, depth, currAging);
        } else {
            fullBuckets++;
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
        colission = 0;
        fullBuckets = 0;
    }

    @Override
    public void collectStatistics(Map stats) {
        stats.put("size", size);
        stats.put("cacheHit", cacheHit);
        stats.put("cacheFail", cacheFail);
        stats.put("colissions", colission);
        stats.put("fullBuckets", fullBuckets);
        if (cacheHit + cacheFail != 0) {
            stats.put("hit/all", cacheHit * 100 / (cacheHit + cacheFail) + "%");
        }
    }

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    public void updateAging(BoardRepresentation board) {
        if (lastBoard == null) {
            currAging = 0;
        } else {
            if (lastBoard.getCastlingRights() != board.getCastlingRights()
                    || figureCount(lastBoard) != figureCount(board)
                    || differentPawnStructure(lastBoard, board)) {
                currAging++;
                if (currAging > 120) {
                    currAging = 0;
                }
                UCILogger.log("TTCache: updated aging");
            }
        }
        lastBoard = board.copy();

        int hitPercent = 0;
        if (cacheHit + cacheFail != 0) {
            hitPercent = cacheHit * 100 / (cacheHit + cacheFail);
        }
        UCILogger.log(format("TTCache: size: %s hits: %s fails: %s %s pct collisions: %s write fail: %s",
                size, cacheHit, cacheFail, hitPercent, colission, fullBuckets));

    }

    private boolean differentPawnStructure(BoardRepresentation board1, BoardRepresentation board2) {
        long pawnMask1 = createPawnMask(board1);
        long pawnMask2 = createPawnMask(board2);
        return pawnMask1 != pawnMask2;

    }

    private long createPawnMask(BoardRepresentation board) {
        long mask = 0L;

        for (int pawn : board.getWhitePieces().getPawns().getArr()) {
            mask |= (1L << pawn);
        }
        for (int pawn : board.getBlackPieces().getPawns().getArr()) {
            mask |= (1L << pawn);
        }

        return mask;
    }

    private int figureCount(BoardRepresentation board) {

        return board.getWhitePieces().getPawns().size()
                + board.getWhitePieces().getKnights().size()
                + board.getWhitePieces().getBishops().size()
                + board.getWhitePieces().getRooks().size()
                + board.getWhitePieces().getQueens().size()
                + board.getBlackPieces().getPawns().size()
                + board.getBlackPieces().getKnights().size()
                + board.getBlackPieces().getBishops().size()
                + board.getBlackPieces().getRooks().size()
                + board.getBlackPieces().getQueens().size();

    }
}
