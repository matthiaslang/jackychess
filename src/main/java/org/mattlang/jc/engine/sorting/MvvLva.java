package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.moves.MoveImpl;

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

    /**
     * array indexed by figure code to return a weight
     */
    private static int[] weights = new int[256];

    /**
     * array indexed by figuretype | capturedFigure << 5 to point to a precalculated mvvLva value.
     */
    private static int[] mvvLvaMatrix = new int[2 << 10];

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

        /**
         * prefill calculated mmvla values:
         */
        for (Figure f1 : Figure.values()) {
            if (f1 != Figure.EMPTY) {
                for (Figure f2 : Figure.values()) {
                    if (f2 != Figure.EMPTY && f1 != f2) {
                        byte capturedFigure = f2.figureCode;
                        byte figureType = f1.figureCode;
                        byte figureTypeUMask =     (byte) (f1.figureCode & MASK_OUT_COLOR);

                        // add two values, one for captures, one for quiet move:
                        mvvLvaMatrix[figureType | capturedFigure << 5] = calcMMVLVA(figureType, capturedFigure);
                        mvvLvaMatrix[figureType | 0 << 5] = calcMMVLVA(figureType, (byte) 0);
                        // add it also for the figure code without mask (makes some duplicates but does not matter):
                        // but this is the index, that is usually used during game
                        mvvLvaMatrix[figureTypeUMask | capturedFigure << 5] = calcMMVLVA(figureType, capturedFigure);
                        mvvLvaMatrix[figureTypeUMask | 0 << 5] = calcMMVLVA(figureType, (byte) 0);
                    }
                }
            }
        }

    }

    /**
     * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
     */

    public static int calcMMVLVA(Move move) {
        if (move.isCapture()) {
            // for a capture build the difference to order for good and bad captures:
            return weights[move.getCapturedFigure()] - weights[move.getFigureType()];
        } else {
            // non captures are the figure wheight div 10 to distinguish the value from "bad" captures by factor 10:
            return -weights[move.getFigureType()];
        }
    }

    public static int calcMMVLVA(byte figureType, byte capturedFigure) {
        if (capturedFigure != 0) {
            // for a capture build the difference to order for good and bad captures:
            return weights[capturedFigure] - weights[figureType];
        } else {
            // non captures are the negative figure weight
            return -weights[figureType];
        }
    }

    public static int calcMMVLVA(int moveInt) {
        byte capturedFigure = MoveImpl.getCapturedFigure(moveInt);
        byte figureType = MoveImpl.getFigureType(moveInt);
        int index = figureType | capturedFigure << 5;
        return mvvLvaMatrix[index];
    }

}
