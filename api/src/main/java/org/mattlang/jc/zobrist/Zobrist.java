package org.mattlang.jc.zobrist;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.FigureConstants.*;

import java.util.Random;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;

public final class Zobrist {

    // max field pos: 64 board files + 1 "virtual" für en passten unset (-1)
    private static final int MAXPOS = NUM_BOARD_FIELDS + 1;
    // 12 figures encoded by their figure code + 2 en passant
    private static final int MAXFIG = MAX_FIGURE_INDEX + 2;
    // castling rights are saved in the board by a nibble, so use 16 rnd values
    private static final int MAXCASTLING = 16;

    /* last index is used for en passant encoding. */
    private static final int ENPASSANT_MOVE_TARGET_POS_IDX = MAXFIG - 1;

    private static long[][] rnd = new long[MAXPOS][MAXFIG];

    private static long[] rndCastling = new long[MAXCASTLING];

    private static long colorFlip;

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

    public static long hash(BoardRepresentation board) {
        long h = 0;
        for (int i = 0; i < NUM_BOARD_FIELDS; i++) {
            byte fig = board.getFigureCode(i);
            if (fig != FigureConstants.FT_EMPTY) {
                h = updateFig(h, i, fig);
            }
        }
        h = updateEnPassant(h, board.getEnPassantMoveTargetPos());
        h = updateCastling(h, board.getCastlingRights());
        if (board.getSiteToMove() == BLACK) {
            h = Zobrist.colorFlip(h);
        }

        return h;
    }

    public static long hashPawnsAndKings(BoardRepresentation board) {
        long h = 0;
        for (int i = 0; i < NUM_BOARD_FIELDS; i++) {
            byte fig = board.getFigureCode(i);
            if (isKingOrPawn(fig)) {
                h = updateFig(h, i, fig);
            }
        }
        return h;
    }

    public static boolean isKingOrPawn(byte figureCode) {
        byte figureType = (byte) (figureCode & MASK_OUT_COLOR);
        return figureType == FT_PAWN || figureType == FT_KING;
    }

    public static long updateFig(long h, int i, byte fig) {
        return h ^ rnd[i][fig];
    }

    public static long updateEnPassant(long h, int enPassantMoveTargetPos) {
        return h ^ rnd[enPassantMoveTargetPos + 1][ENPASSANT_MOVE_TARGET_POS_IDX];
    }

    public static long updateCastling(long h, byte castlingRights) {
        return h ^ rndCastling[castlingRights];
    }

    public static long colorFlip(long h) {
        return h ^ colorFlip;
    }
}
