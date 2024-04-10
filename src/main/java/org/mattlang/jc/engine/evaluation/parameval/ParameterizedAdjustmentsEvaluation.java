package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

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

    @EvalConfigParam(configName = "tempo", mgEgCombined = true)
    private int tempoMgEg;

    @EvalConfigParam(configName = "bishopPair", mgEgCombined = true)
    private int bishopPairMgEg;

    @EvalConfigParam(configName = "knightPair", mgEgCombined = true)
    private int knightPairMgEg;

    @EvalConfigParam(configName = "rookPair", mgEgCombined = true)
    private int rookPairMgEg;

    @EvalConfigParam(configName = "knightAdj")
    public MgEgArrayFunction knightAdjustmentMGEG;

    @EvalConfigParam(configName = "rookAdj")
    public MgEgArrayFunction rookAdjustmentMGEG;

    public ParameterizedAdjustmentsEvaluation(EvalConfig config) {
    }

    public int adjust(BitChessBoard bb, Color who2Move) {
        int result = 0;
        /* tempo bonus */
        if (who2Move == WHITE)
            result += tempoMgEg;
        else
            result -= tempoMgEg;

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
            adjWhite += bishopPairMgEg;
        if (blackBishops > 1)
            adjBlack += bishopPairMgEg;
        if (whiteKnights > 1)
            adjWhite -= knightPairMgEg;
        if (blackKnights > 1)
            adjBlack -= knightPairMgEg;
        if (whiteRooks > 1)
            adjWhite -= rookPairMgEg;
        if (blackRooks > 1)
            adjBlack -= rookPairMgEg;

        adjWhite += knightAdjustmentMGEG.calc(whitePawns) * whiteKnights;
        adjBlack += knightAdjustmentMGEG.calc(blackPawns) * blackKnights;
        adjWhite += rookAdjustmentMGEG.calc(whitePawns) * whiteRooks;
        adjBlack += rookAdjustmentMGEG.calc(blackPawns) * blackRooks;

        return result + (adjWhite - adjBlack);
    }
}
