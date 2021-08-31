package org.mattlang.jc.engine.sorting;

import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.Weights;

/**
 * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
 * see https://www.chessprogramming.org/MVV-LVA.
 *
 * Higher Value, the more in front of move ordering.
 *
 * Captures:
 * best value: Pawn x Queen == (900-100) + 5000 == 5800
 * middle value:
 *              Knight x Knight = (300-300) +5000== 5000;
 *              Knight X Rook   = (500-300) +5000== 5200
 * worst value: Queen x Pawn = (100-900) +5000= 4200
 *
 * no Captures:
 *
 * best value: Pawn  == 100/10 == -10
 * worst value: Queen  = 900/10 = -90
 */
public class MvvLva {

    private static int[] weights = new int[256];

    private static int CAPTURE_BONUS = 5000;

    static {
        weights[Figure.B_Pawn.figureCode] = Weights.PAWN_WEIGHT;
        weights[Figure.W_Pawn.figureCode] = Weights.PAWN_WEIGHT;

        weights[Figure.B_Knight.figureCode] = Weights.KNIGHT_WEIGHT;
        weights[Figure.W_Knight.figureCode] = Weights.KNIGHT_WEIGHT;

        weights[Figure.B_Bishop.figureCode] = Weights.BISHOP_WEIGHT;
        weights[Figure.W_Bishop.figureCode] = Weights.BISHOP_WEIGHT;

        weights[Figure.B_Rook.figureCode] = Weights.ROOK_WEIGHT;
        weights[Figure.W_Rook.figureCode] = Weights.ROOK_WEIGHT;

        weights[Figure.B_Queen.figureCode] = Weights.QUEEN_WEIGHT;
        weights[Figure.W_Queen.figureCode] = Weights.QUEEN_WEIGHT;

        weights[Figure.B_King.figureCode] = Weights.KING_WEIGHT + Weights.PAWN_WEIGHT;
        weights[Figure.W_King.figureCode] = Weights.KING_WEIGHT + Weights.PAWN_WEIGHT;
    }

    /**
     * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
     */

    public static int calcMMVLVA(Move move) {
        if (move.isCapture()) {
            // for a capture build the difference to order for good and bad captures:
            return weights[move.getCapturedFigure()] - weights[move.getFigureType()] ;
        } else {
            // non captures are the figure wheight div 10 to distinguish the value from "bad" captures by factor 10:
            return -weights[move.getFigureType()] ;
        }
    }
}
