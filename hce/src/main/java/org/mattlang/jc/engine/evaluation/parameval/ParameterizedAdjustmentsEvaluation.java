package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.Getter;

/**
 * Paremeterized positional and material adjustments.
 */
@Getter
@EvalConfigurable(prefix = "adjustments")
@EvalValueInterval(min = -2000, max = 2000)
public class ParameterizedAdjustmentsEvaluation {

    @EvalConfigParam(mgEgCombined = true)
    private int tempo;

    @EvalConfigParam(mgEgCombined = true)
    private int bishopPair;

    @EvalConfigParam(mgEgCombined = true)
    private int knightPair;

    @EvalConfigParam(mgEgCombined = true)
    private int rookPair;

    @EvalConfigParam
    public MgEgArrayFunction knightAdj;

    @EvalConfigParam
    public MgEgArrayFunction rookAdj;

    public ParameterizedAdjustmentsEvaluation() {
    }

    public int adjust(BitChessBoard bb, Color who2Move) {
        int result = 0;
        /* tempo bonus */
        if (who2Move == WHITE)
            result += tempo;
        else
            result -= tempo;

        int adjWhite = 0;
        int adjBlack = 0;

        /**************************************************************************
         *  Adjusting material value for the various combinations of pieces.       *
         *  Currently it scores bishop, knight and rook pairs. The first one       *
         *  gets a bonus, the latter two - a penalty. Beside that knights lose     *
         *  value as pawns disappear, whereas rooks gain.                          *
         **************************************************************************/

        int whitePawns = bb.getPawnsCount(nWhite);
        int blackPawns = bb.getPawnsCount(nBlack);

        int whiteBishops = bb.getBishopsCount(nWhite);
        int blackBishops = bb.getBishopsCount(nBlack);

        int whiteKnights = bb.getKnightsCount(nWhite);
        int blackKnights = bb.getKnightsCount(nBlack);

        int whiteRooks = bb.getRooksCount(nWhite);
        int blackRooks = bb.getRooksCount(nBlack);

        if (whiteBishops > 1)
            adjWhite += bishopPair;
        if (blackBishops > 1)
            adjBlack += bishopPair;
        if (whiteKnights > 1)
            adjWhite -= knightPair;
        if (blackKnights > 1)
            adjBlack -= knightPair;
        if (whiteRooks > 1)
            adjWhite -= rookPair;
        if (blackRooks > 1)
            adjBlack -= rookPair;

        adjWhite += knightAdj.calc(whitePawns) * whiteKnights;
        adjBlack += knightAdj.calc(blackPawns) * blackKnights;
        adjWhite += rookAdj.calc(whitePawns) * whiteRooks;
        adjBlack += rookAdj.calc(blackPawns) * blackRooks;

        return result + (adjWhite - adjBlack);
    }
}
