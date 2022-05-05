package org.mattlang.jc.zobrist;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.FigureConstants.*;

import java.util.Random;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;

import lombok.Getter;

public final class Zobrist {

    // max field pos: 64 board files + 1 "virtual" für en passten unset (-1)
    private static final int MAXPOS = 64 + 1;
    // 12 figures (6 white, 6 black) + 2 en passant
    private static final int MAXFIG = 6 + 6 + 2;
    // castling rights are saved in the board by a nibble, so use 16 rnd values
    private static final int MAXCASTLING = 16;
    private static final int ENPASSANT_MOVE_TARGET_POS_IDX = 13;

    private static long[][] rnd = new long[MAXPOS][MAXFIG];

    private static long[] rndCastling = new long[MAXCASTLING];

    private static long colorFlip;

    @Getter
    private long hash = 0;

    static {
        initRandomNumbers();
    }

    public static void initRandomNumbers() {
        Random random = new Random(4228109200L);
        for (int i = 0; i < MAXPOS; i++) {
            for (int j = 0; j < MAXFIG; j++) {
                rnd[i][j] = random.nextLong();
            }
        }
        for (int i = 0; i < MAXCASTLING; i++) {
            rndCastling[i] = random.nextLong();
        }

        colorFlip = random.nextLong();
    }

    public void setHash(long val) {
        hash = val;
    }

    public void init(BoardRepresentation board) {
        hash = 0;
        for (int i = 0; i < 64; i++) {
            byte fig = board.getFigureCode(i);
            if (fig != FigureConstants.FT_EMPTY) {
                addFig(i, fig);
            }
        }
        updateEnPassant(board.getEnPassantMoveTargetPos());
        updateCastling(board.getCastlingRights());
        if (board.getSiteToMove() == BLACK) {
            colorFlip();
        }
    }

    public void initPawnHash(BoardRepresentation board) {
        hash = 0;
        for (int i = 0; i < 64; i++) {
            byte fig = board.getFigureCode(i);
            if (fig != FigureConstants.FT_EMPTY) {
                if (fig == W_PAWN || fig == B_PAWN) {
                    addFig(i, fig);
                }
            }
        }
        if (board.getSiteToMove() == BLACK) {
            colorFlip();
        }
    }

    public void addFig(int i, byte fig) {
        int blackoffset = (fig & BLACK.code) == BLACK.code ? 6 : 0;
        int figIndex = (fig & MASK_OUT_COLOR) + blackoffset;
        hash ^= rnd[i][figIndex];
    }

    public void removeFig(int i, byte fig) {
        int blackoffset = (fig & BLACK.code) == BLACK.code ? 6 : 0;
        int figIndex = (fig & MASK_OUT_COLOR) + blackoffset;
        hash ^= rnd[i][figIndex];
    }

    public void move(int from, int to, byte fig) {
        int blackoffset = (fig & BLACK.code) == BLACK.code ? 6 : 0;
        int figIndex = (fig & MASK_OUT_COLOR) + blackoffset;
        hash ^= rnd[from][figIndex] ^ rnd[to][figIndex];
    }

    public void updateEnPassant(int enPassantMoveTargetPos) {
        hash ^= rnd[enPassantMoveTargetPos + 1][ENPASSANT_MOVE_TARGET_POS_IDX];
    }

    public void updateCastling(byte castlingRights) {
        hash ^= rndCastling[castlingRights];
    }

    public void colorFlip() {
        hash ^= colorFlip;
    }

}
