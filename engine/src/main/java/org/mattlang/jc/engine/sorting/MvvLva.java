package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.board.FigureConstants.FT_QUEEN;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

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
     * Calcs the MMV-LVA   (Most Valuable Victim - Least Valuable Aggressor) simple order heuristic,
     */

    public static int calcMMVLVA(Move move) {
        int weight = 0;
        if (move.isCapture()) {
            int captFigType = move.getCapturedFigure() & MASK_OUT_COLOR;
            // for a capture build the difference to order for good and bad captures:
            weight = captFigType * 6 - move.getFigureType();
        } else {
            // non captures are the figure wheight div 10 to distinguish the value from "bad" captures by factor 10:
            weight = -move.getFigureType();
        }
        // weight "high" promotions (queen promotion);
        // all other promotions are mostly not of interest(bishop & rook are redundant to queen; Knight is mostly not desirable)
        if (move.isPromotion() && move.getPromotedFigure().figureType == FigureType.Queen) {
            weight += (FT_QUEEN + 1) * 6;
        }
        return weight;
    }

}
