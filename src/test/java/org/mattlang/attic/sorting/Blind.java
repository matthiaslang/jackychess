package org.mattlang.attic.sorting;

import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.movegenerator.Captures.canFigureCaptured;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.Move;

/**
 * Simple BLIND order calculation. (Not used since we use SEE to order good captures).
 */
public class Blind {

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

    /******************************************************************************
     *  This is not yet proper static exchange evaluation, but an approximation    *
     *  proposed by Harm Geert Mueller under the acronym BLIND (better, or lower   *
     *  if not defended. As the name indicates, it detects only obviously good     *
     *  captures, but it seems enough to improve move ordering.                    *
     ******************************************************************************/

    public static boolean blind(BoardRepresentation board, Move move) {
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
