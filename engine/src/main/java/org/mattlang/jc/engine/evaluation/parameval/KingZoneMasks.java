package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.BB.*;
import static org.mattlang.jc.board.Color.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Tools;

/**
 * Calculation of king zone mask used in evaluation.
 * Holds precalculated zone masks.
 */
public class KingZoneMasks {

    private static long[][] kingZoneMasks = new long[2][64];

    static {
        for (int i = 0; i < 64; i++) {
            long kingMask = 1L << i;
            kingZoneMasks[nWhite][i] = createKingZoneMask(i, kingMask, WHITE);
            kingZoneMasks[nBlack][i] = createKingZoneMask(i, kingMask, BLACK);
        }
    }

    public static long getKingZoneMask(int color, int kingPos) {
        return kingZoneMasks[color][kingPos];
    }

    /**
     * King Zone:
     *
     * The king pos + all 9 fields around (the king attack fields) + 3 front fields (pawn fields)
     * if on A or H, we add also the C or F fields respectively to the mask
     *
     * @param kingMask
     * @param xside
     * @return
     */
    public static long createKingZoneMask(int kingSquare, long kingMask, Color xside) {
        long frontMask = xside == WHITE ? kingAttacks(nortOne(kingMask)) : kingAttacks(soutOne(kingMask));
        long mask = kingMask | kingAttacks(kingMask) | frontMask;
        if (Tools.fileOf(kingSquare) == 0) {
            mask |= mask << 1;
        } else if (Tools.fileOf(kingSquare) == 7) {
            mask |= mask >>> 1;
        }

        return mask;
    }
}
