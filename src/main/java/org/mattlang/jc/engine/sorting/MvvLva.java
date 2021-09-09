package org.mattlang.jc.engine.sorting;

import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.Move;

/**
 * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
 * see https://www.chessprogramming.org/MVV-LVA.
 *
 * Higher Value, the more in front of move ordering.
 *
 * Captures:
 * best value: Pawn x Queen == (5*6-1)  == 29
 * middle value:
 * Knight x Knight = (2*6-2) == 10;
 * Knight X Rook   = (4*6-4) == 20
 * worst value: Queen x Pawn = (1*6-5) = 1
 *
 * no Captures give negativ numbers:
 *
 * best value: Pawn  == -1
 * worst value: Queen  = -5
 */
public class MvvLva {

    /**
     * array indexed by figure code to return a weight
     */
    private static int[] weights = new int[256];

    /**
     * array indexed by figuretype | capturedFigure << 5 to point to a precalculated mvvLva value.
     */
    private static int[] mvvLvaMatrix = new int[2 << 10];

    public static final int MVVLVA_PAWN = 1;
    public static final int MVVLVA_KNIGHT = 2;
    public static final int MVVLVA_BISHOP = 3;
    public static final int MVVLVA_ROOK = 4;
    public static final int MVVLVA_QUEEN = 5;
    public static final int MVVLVA_KING = 6;

    static {
        weights[FigureType.Pawn.figureCode] = MVVLVA_PAWN;
        weights[Figure.B_Pawn.figureCode] = MVVLVA_PAWN;
        weights[Figure.W_Pawn.figureCode] = MVVLVA_PAWN;

        weights[FigureType.Knight.figureCode] = MVVLVA_KNIGHT;
        weights[Figure.B_Knight.figureCode] = MVVLVA_KNIGHT;
        weights[Figure.W_Knight.figureCode] = MVVLVA_KNIGHT;

        weights[FigureType.Bishop.figureCode] = MVVLVA_BISHOP;
        weights[Figure.B_Bishop.figureCode] = MVVLVA_BISHOP;
        weights[Figure.W_Bishop.figureCode] = MVVLVA_BISHOP;

        weights[FigureType.Rook.figureCode] = MVVLVA_ROOK;
        weights[Figure.B_Rook.figureCode] = MVVLVA_ROOK;
        weights[Figure.W_Rook.figureCode] = MVVLVA_ROOK;

        weights[FigureType.Queen.figureCode] = MVVLVA_QUEEN;
        weights[Figure.B_Queen.figureCode] = MVVLVA_QUEEN;
        weights[Figure.W_Queen.figureCode] = MVVLVA_QUEEN;

        weights[FigureType.King.figureCode] = MVVLVA_KING;
        weights[Figure.B_King.figureCode] = MVVLVA_KING;
        weights[Figure.W_King.figureCode] = MVVLVA_KING;

    }

    /**
     * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
     */

    public static int calcMMVLVA(Move move) {
        int weight = 0;
        if (move.isCapture()) {
            // for a capture build the difference to order for good and bad captures:
            weight = weights[move.getCapturedFigure()] * 6 - weights[move.getFigureType()];
        } else {
            // non captures are the figure wheight div 10 to distinguish the value from "bad" captures by factor 10:
            weight = -weights[move.getFigureType()];
        }
        // weight "high" promotions (queen promotion);
        // all other promotions are mostly not of interest(bishop & rook are redundant to queen; Knight is mostly not desirable)
        if (move.isPromotion() && move.getPromotedFigure().figureType == FigureType.Queen) {
            weight += (FigureType.Queen.figureCode + 1) * 6;
        }
        return weight;
    }

}
