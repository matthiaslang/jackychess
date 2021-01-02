package org.mattlang.jc.zobrist;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;

import java.util.Random;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

public class Zobrist {

    // max field pos: 64 board files + 1 "virtual" für en passten unset (-1)
    private static final int MAXPOS = 64 + 1;
    // 12 figures (6 white, 6 black) + 2 en passant
    private static final int MAXFIG = 6 + 6 + 2;
    // castling rights are saved in the board by a nibble, so use 16 rnd values
    private static final int MAXCASTLING = 16;
    private static final int ENPASSANT_CAP_POS_IDX = 12;
    private static final int ENPASSANT_MOVE_TARGET_POS_IDX = 13;

    private static long[][] rnd = new long[MAXPOS][MAXFIG];

    private static long[] rndCastling = new long[MAXCASTLING];

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
    }

    public static long hash(BoardRepresentation board) {
        long h = 0;
        for (int i = 0; i < 64; i++) {
            byte fig = board.getFigureCode(i);
            if (fig != FigureConstants.FT_EMPTY) {
                h = addFig(h, i, fig);
            }
        }
        h = updateEnPassant(h, board.getEnPassantCapturePos(), board.getEnPassantMoveTargetPos());
        h = updateCastling(h, board.getCastlingRights());

        return h;
    }

    public static long addFig(long h, int i, byte fig) {
        int blackoffset = (fig & BLACK.code) == BLACK.code ? 6 : 0;
        int figIndex = (fig & MASK_OUT_COLOR) + blackoffset;
        h ^= rnd[i][figIndex];
        return h;
    }

    public static long removeFig(long h, int i, byte fig) {
        int blackoffset = (fig & BLACK.code) == BLACK.code ? 6 : 0;
        int figIndex = (fig & MASK_OUT_COLOR) + blackoffset;
        h ^= rnd[i][figIndex];
        return h;
    }

    public static long updateEnPassant(long h, int enPassantCapturePos, int enPassantMoveTargetPos) {
        h ^= rnd[enPassantCapturePos + 1][ENPASSANT_CAP_POS_IDX];
        h ^= rnd[enPassantMoveTargetPos + 1][ENPASSANT_MOVE_TARGET_POS_IDX];
        return h;
    }

    public static long updateCastling(long h, byte castlingRights) {
        h ^= rndCastling[castlingRights];
        return h;
    }
}
