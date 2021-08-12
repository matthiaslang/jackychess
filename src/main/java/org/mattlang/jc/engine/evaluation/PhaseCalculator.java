package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;

/**
 * Calculates a factor for the game phase based on the existing material.
 * 1 == opening game
 * 0 == end game
 * everything else is somewhere inbetween.
 */
public class PhaseCalculator {

    private static final int Midgame = 5255;
    private static final int Endgame = 435;

    private static final int[] PhaseValues = new int[] { 0, 155, 305, 405, 1050, 0 };

    public static double Linstep(double edge0, double edge1, double v) {
        return Math.min(1, Math.max(0, (v - edge0) / (edge1 - edge0)));
    }

    /**
     * Calculates a phase factor (1 == Middle Game; 0 == Endgame, or something between)
     *
     * @param currBoard
     * @return
     */
    public static double calcPhaseFactor(BoardRepresentation currBoard) {
        int phase = 0;

        for (int i = 0; i < 64; ++i) {
            byte figure = currBoard.getFigureCode(i);
            if (figure != FigureConstants.FT_EMPTY) {

                byte pieceIndex = (byte) (figure & MASK_OUT_COLOR);
                phase += PhaseValues[pieceIndex];
            }
        }

        double factor = Linstep(Endgame, Midgame, phase);
        return factor;
    }

    public static double scaleByPhase(BoardRepresentation currBoard, int midGame, int endGame) {
        double factor = calcPhaseFactor(currBoard);
        double score = factor * midGame + (1 - factor) * endGame;
        return score;
    }

    public static boolean isOpeningOrMiddleGame(BoardRepresentation currBoard) {
        return calcPhaseFactor(currBoard) > 0.5;
    }
}
