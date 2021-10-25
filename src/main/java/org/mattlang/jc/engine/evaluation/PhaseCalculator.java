package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * Calculates a factor for the game phase based on the existing material.
 * 1 == opening game
 * 0 == end game
 * everything else is somewhere inbetween.
 */
public class PhaseCalculator {

    private static final int Midgame = 5255;
    private static final int Endgame = 435;

    public static final int PV_PAWN = 0;
    public static final int PV_KNIGHT = 155;
    public static final int PV_BISHOP = 305;
    public static final int PV_ROOK = 405;
    public static final int PV_QUEEN = 1050;
    public static final int PV_KING = 0;
    private static final int[] PhaseValues = new int[] { PV_PAWN, PV_KNIGHT, PV_BISHOP, PV_ROOK, PV_QUEEN, PV_KING };

    public static double Linstep(double edge0, double edge1, double v) {
        return Math.min(1, Math.max(0, (v - edge0) / (edge1 - edge0)));
    }

    /**
     * Calculates a phase factor (1 == Middle Game; 0 == Endgame, or something between)
     *
     * @param currBoard
     * @return
     */
    public static double calcPhaseFactorOld(BoardRepresentation currBoard) {
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

    public static double calcPhaseFactor(BoardRepresentation currBoard) {
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();
        return calcPhaseFactor(wp, bp);
    }

    public static double calcPhaseFactor(PieceList wp, PieceList bp) {
        int phase = 0;

        phase += (wp.getKnights().size() + bp.getKnights().size()) * PV_KNIGHT +
                (wp.getBishops().size() + bp.getBishops().size()) * PV_BISHOP +
                (wp.getRooks().size() + bp.getRooks().size()) * PV_ROOK +
                (wp.getQueens().size() + bp.getQueens().size()) * PV_QUEEN;

        double factor = Linstep(Endgame, Midgame, phase);
        return factor;
    }

    public static double calcPhaseFactor(BitChessBoard bb) {
        int phase = 0;

        phase += bb.getKnightsCount() * PV_KNIGHT +
                bb.getBishopsCount() * PV_BISHOP +
                bb.getRooksCount() * PV_ROOK +
                bb.getQueensCount() * PV_QUEEN;

        double factor = Linstep(Endgame, Midgame, phase);
        return factor;
    }

    public static double scaleByPhase(BoardRepresentation currBoard, int midGame, int endGame) {
        double factor = calcPhaseFactor(currBoard);
        double score = factor * midGame + (1 - factor) * endGame;
        return score;
    }

    public static double scaleByPhase(BitChessBoard bb, int midGame, int endGame) {
        double factor = calcPhaseFactor(bb);
        double score = factor * midGame + (1 - factor) * endGame;
        return score;
    }

    public static double scaleByPhase(PieceList wp, PieceList bp, int midGame, int endGame) {
        double factor = calcPhaseFactor(wp, bp);
        double score = factor * midGame + (1 - factor) * endGame;
        return score;
    }

    public static boolean isOpeningOrMiddleGame(BoardRepresentation currBoard) {
        return calcPhaseFactor(currBoard) > 0.5;
    }
}
