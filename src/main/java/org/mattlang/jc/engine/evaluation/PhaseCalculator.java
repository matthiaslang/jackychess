package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * Calculates a factor for the game phase based on the existing material.
 * 1 == opening game
 * 0 == end game
 * everything else is somewhere inbetween.
 */
public class PhaseCalculator {

    private static final int MIDGAME = 5255;
    private static final int ENDGAME = 435;

    public static final int PV_KNIGHT = 155;
    public static final int PV_BISHOP = 305;
    public static final int PV_ROOK = 405;
    public static final int PV_QUEEN = 1050;

    private static double linstep(double edge0, double edge1, double v) {
        return Math.min(1, Math.max(0, (v - edge0) / (edge1 - edge0)));
    }

    public static double calcPhaseFactor(BoardRepresentation currBoard) {
        return calcPhaseFactor((currBoard).getBoard());
    }

    public static double calcPhaseFactor(BitChessBoard bb) {
        int phase = 0;

        phase += bb.getKnightsCount() * PV_KNIGHT +
                bb.getBishopsCount() * PV_BISHOP +
                bb.getRooksCount() * PV_ROOK +
                bb.getQueensCount() * PV_QUEEN;

        return linstep(ENDGAME, MIDGAME, phase);
    }


    public static double scaleByPhase(BitChessBoard bb, int midGame, int endGame) {
        double factor = calcPhaseFactor(bb);
        return factor * midGame + (1 - factor) * endGame;
    }

    public static boolean isOpeningOrMiddleGame(BoardRepresentation currBoard) {
        return calcPhaseFactor(currBoard) > 0.5;
    }
}
