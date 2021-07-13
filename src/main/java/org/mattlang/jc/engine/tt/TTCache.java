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

    private int cacheHit;
    private int cacheFail;
    private int colission;
    private int size;

    /**
     * the current aging value.
     */
    private byte currAging = 0;
    /**
     * the last board representation to check for aging.
     */
    private BoardRepresentation lastBoard;

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
        return currAging - entry.getAging() < 10;
    }

    public final void storeTTEntry(BoardRepresentation board, Color side, int eval, byte tpe, int depth) {
        long boardZobristHash = board.getZobristHash();

        // only store entries with lower depth:
        TTEntry existing = getTTEntry(board, side);
        if (existing == null) {
            storeTT(boardZobristHash, side, new TTEntry(boardZobristHash, eval, tpe, depth));
        } else if (existing.isEmpty() || existing.depth > depth || existing.getAging() != currAging) {
            existing.update(boardZobristHash, eval, tpe, depth, currAging);
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

    private final int h0(long key) {
        return (int) (key & (CAPACITY - 1));
    }

    private final int h1(long key) {
        return (int) ((key >> 32) & (CAPACITY - 1));
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
        UCILogger.log(format("TTCache: size: %s hits: %s fails: %s %s pct collisions: %s",
                size, cacheHit, cacheFail, hitPercent, colission));

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
