package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.movegenerator.BBMoveGeneratorImpl.canFigureCaptured;

import org.mattlang.jc.board.BoardRepresentation;
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

    /******************************************************************************
     *  This is not yet proper static exchange evaluation, but an approximation    *
     *  proposed by Harm Geert Mueller under the acronym BLIND (better, or lower   *
     *  if not defended. As the name indicates, it detects only obviously good     *
     *  captures, but it seems enough to improve move ordering.                    *
     ******************************************************************************/

    public boolean blind(BoardRepresentation board, Move move) {
        int sq_to = move.getToIndex();
        int sq_fr = move.getFromIndex();

        byte pc_fr = move.getFigureType();

        /* captures by pawn do not lose material */
        if (pc_fr == FT_PAWN)
            return true;

        byte captFigure = move.getCapturedFigure();

        int weightFig = weights[pc_fr];
        int weightCapFig = weights[captFigure];

        /* Captures "lower takes higher" (as well as BxN) are good by definition. */
        if (weightCapFig > weightFig || weightFig == MVVLVA_BISHOP && weightCapFig == MVVLVA_KNIGHT)
            return true;

        Figure fig = board.getPos(sq_fr);
        /* Make the first capture, so that X-ray defender show up*/
        board.setPos(sq_fr, Figure.EMPTY);

        /* Captures of undefended pieces are good by definition */
        boolean blind = !canFigureCaptured(board, sq_to, board.getSiteToMove());
        board.setPos(sq_fr, fig);

        return blind;
    }

}
