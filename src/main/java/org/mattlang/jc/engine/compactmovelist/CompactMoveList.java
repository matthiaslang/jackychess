package org.mattlang.jc.engine.compactmovelist;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import static org.mattlang.jc.board.Color.WHITE;

/**
 * Movelist saved in a byte array. 4 bytes per move.
 * Coding:
 *
 * 0 byte: from
 * 1 byte: to
 * 3 byte: capturedFigure
 * 4 byte: special markers e.g. rochade
 *
 * Surprisingly it seems not so performant as the BasicMovelist due to the fact,
 * that it needs much time to initialize the movelist bytearray... (see LegalMoveCacheTest)
 */
public class CompactMoveList implements MoveList {

    private static final byte ROCHADE_MOVE = 16;
    private static final byte PAWN_MOVE = 32;
    private static final byte PAWN_PROMOTION = 64;

    protected byte[][] movelist = new byte[60][4];

    public static final int IDX_FROM = 0;
    public static final int IDX_TO = 1;
    public static final int IDX_CAPTURE = 2;
    public static final int IDX_SPECIAL = 3;

    private int size = 0;

    @Override
    public void genMove(int from, int to, byte capturedFigure) {
        movelist[size][IDX_FROM] = (byte) from;
        movelist[size][IDX_TO] = (byte) to;
        if (capturedFigure != 0) {
            movelist[size][IDX_CAPTURE] = capturedFigure;
        }
        size++;
    }

    @Override
    public void genPawnMove(int from, int to, Color side, byte capturedFigure, int enPassantOption) {
        movelist[size][IDX_FROM] = (byte) from;
        movelist[size][IDX_TO] = (byte) to;
        if (capturedFigure != 0) {
            movelist[size][IDX_CAPTURE] = capturedFigure;
        }
        movelist[size][IDX_SPECIAL] = PAWN_MOVE;
        boolean isOnLastLine = false;
        if (side == WHITE) {
            isOnLastLine = to >= 56 && to <= 63;
        } else {
            isOnLastLine = to >= 0 && to <= 7;
        }
        if (isOnLastLine) {
            movelist[size][IDX_SPECIAL] |= PAWN_PROMOTION;
        }

        size++;
    }

    private static byte[] ROCHADE_LONG_WHITE = { 4, 2, 0, 3 };
    private static byte[] ROCHADE_SHORT_WHITE = { 4, 6, 7, 5 };
    private static byte[] ROCHADE_SHORT_BLACK = { 60, 62, 63, 61 };
    private static byte[] ROCHADE_LONG_BLACK = { 60, 58, 56, 59 };

    private static byte[][] ROCHADE_ALL = { ROCHADE_LONG_WHITE, ROCHADE_SHORT_WHITE, ROCHADE_SHORT_BLACK, ROCHADE_LONG_BLACK };

    private static byte[] findRochade(byte from, byte to) {
        for (byte[] rochade : ROCHADE_ALL) {
            if (rochade[IDX_FROM] == from && rochade[IDX_TO] == to) {
                return rochade;
            }
        }
        throw new IllegalStateException("could not find rochade type!");
    }

    @Override
    public void addRochadeLongWhite() {
        movelist[size][IDX_FROM] = ROCHADE_LONG_WHITE[IDX_FROM];
        movelist[size][IDX_TO] = ROCHADE_LONG_WHITE[IDX_TO];
        movelist[size][IDX_SPECIAL] = ROCHADE_MOVE;

        size++;
    }

    @Override
    public void addRochadeShortWhite() {
        movelist[size][IDX_FROM] = ROCHADE_SHORT_WHITE[IDX_FROM];
        movelist[size][IDX_TO] = ROCHADE_SHORT_WHITE[IDX_TO];
        movelist[size][IDX_SPECIAL] = ROCHADE_MOVE;
        size++;

    }

    @Override
    public void addRochadeShortBlack() {
        movelist[size][IDX_FROM] = ROCHADE_SHORT_BLACK[IDX_FROM];
        movelist[size][IDX_TO] = ROCHADE_SHORT_BLACK[IDX_TO];
        movelist[size][IDX_SPECIAL] = ROCHADE_MOVE;
        size++;

    }

    @Override
    public void addRochadeLongBlack() {
        movelist[size][IDX_FROM] = ROCHADE_LONG_BLACK[IDX_FROM];
        movelist[size][IDX_TO] = ROCHADE_LONG_BLACK[IDX_TO];
        movelist[size][IDX_SPECIAL] = ROCHADE_MOVE;
        size++;
    }

    @Override
    public void addMove(MoveCursor moveCursor) {
        CompactMoveCursor compactMoveCursor = (CompactMoveCursor) moveCursor;
        byte[] src = compactMoveCursor.iterator.moveList.movelist[compactMoveCursor.iterator.index];
        movelist[size][IDX_FROM] = src[IDX_FROM];
        movelist[size][IDX_TO] = src[IDX_TO];
        movelist[size][IDX_CAPTURE] = src[IDX_CAPTURE];
        movelist[size][IDX_SPECIAL] = src[IDX_SPECIAL];
        size++;
    }

    @Override
    public boolean capturesFigure(Figure figure) {
        byte figureCode = figure.figureCode;
        for (int i = 0; i < size; i++) {
            if (movelist[i][IDX_CAPTURE] == figureCode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void genEnPassant(int i, int n, Color side, int enPassantCapturePos) {

    }

    @Override
    public Iterator<MoveCursor> iterator() {
        return new CompactMoveListIterator(this);
    }

    public void move(BoardRepresentation board, int index) {
        move(board, movelist[index]);
    }

    public static void move(BoardRepresentation board, byte[] moveEntry) {
        byte from = moveEntry[IDX_FROM];
        byte to = moveEntry[IDX_TO];
        board.move(from, to);
        if ((moveEntry[IDX_SPECIAL] & ROCHADE_MOVE) != 0) {
            byte[] rochade = findRochade(from, to);
            board.move(rochade[2], rochade[3]);
        } else if ((moveEntry[IDX_SPECIAL] & PAWN_PROMOTION) != 0) {
            Figure pawn = board.getPos(to);
            Figure queen = pawn.color == Color.WHITE ? Figure.W_Queen : Figure.B_Queen;
            board.setPos(to, queen);
        }
    }

    public void undoMove(BoardRepresentation board, int index) {
        undoMove(board, movelist[index]);
    }

    public static void undoMove(BoardRepresentation board, byte[] moveEntry) {
        byte from = moveEntry[IDX_FROM];
        byte to = moveEntry[IDX_TO];
        board.move(to, from);
        if (moveEntry[IDX_CAPTURE] != 0) {
            board.setPos(to, moveEntry[IDX_CAPTURE]);
        }

        if ((moveEntry[IDX_SPECIAL] & ROCHADE_MOVE) != 0) {
            byte[] rochade = findRochade(from, to);
            board.move(rochade[3], rochade[2]);
        } else if ((moveEntry[IDX_SPECIAL] & PAWN_PROMOTION) != 0) {

            Figure queen = board.getFigure(from);
            Figure pawn = queen.color == Color.WHITE ? Figure.W_Pawn : Figure.B_Pawn;
            board.setPos(from, pawn);

        }
    }

    public void sortByCapture() {
        Arrays.sort(movelist, new Comparator<byte[]>() {

            @Override
            public int compare(byte[] m1, byte[] m2) {
                return m2[IDX_CAPTURE] - m1[IDX_CAPTURE];
            }
        });
    }
}
