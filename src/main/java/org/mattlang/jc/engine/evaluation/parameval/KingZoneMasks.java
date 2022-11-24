package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BB.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * Calculation of king zone mask used in evaluation.
 * Holds precalculated zone masks.
 */
public class KingZoneMasks {

    private static long[][] kingZoneMasks = new long[2][64];

    static {
        for (int i = 0; i < 64; i++) {
            long kingMask = 1L << i;
            kingZoneMasks[BitChessBoard.nWhite][i] = createKingZoneMask(kingMask, WHITE);
            kingZoneMasks[BitChessBoard.nBlack][i] = createKingZoneMask(kingMask, BLACK);
        }
    }

    public static long getKingZoneMask(int color, int kingPos) {
        return kingZoneMasks[color][kingPos];
    }

    public static long createKingZoneMask(long oppKingMask, Color xside) {
        long frontMask = xside == WHITE ? kingAttacks(nortOne(oppKingMask)) : kingAttacks(soutOne(oppKingMask));
        return kingAttacks(oppKingMask) | frontMask;
    }
}
