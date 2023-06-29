package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedThreatsEvaluation.readCombinedConfigVal;
import static org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction.combine;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;

import lombok.Getter;

/**
 * Paremeterized positional and material adjustments.
 */
@Getter
public class ParameterizedAdjustmentsEvaluation {

    public static final String TEMPO_MG = "tempoMg";
    public static final String TEMPO_EG = "tempoEg";
    public static final String BISHOP_PAIR_MG = "bishopPairMg";
    public static final String BISHOP_PAIR_EG = "bishopPairEg";
    public static final String KNIGHT_PAIR_MG = "knightPairMg";
    public static final String KNIGHT_PAIR_EG = "knightPairEg";
    public static final String ROOK_PAIR_MG = "rookPairMg";
    public static final String ROOK_PAIR_EG = "rookPairEg";

    public static final String KNIGHT_ADJ_MG = "knightAdjMg";
    public static final String KNIGHT_ADJ_EG = "knightAdjEg";

    public static final String ROOK_ADJ_MG = "rookAdjMg";
    public static final String ROOK_ADJ_EG = "rookAdjEg";

    private int tempoMgEg;
    private final ChangeableMgEgScore tempoScore;

    private int bishopPairMgEg;
    private final ChangeableMgEgScore bishopPairScore;

    private int knightPairMgEg;
    private final ChangeableMgEgScore knightPairScore;

    private int rookPairMgEg;
    private final ChangeableMgEgScore rookPairScore;

    /* adjustements of piece value based on the number of own pawns */
    //    int n_adj[] = { -20, -16, -12, -8, -4, 0, 4, 8, 12 };
    //    int r_adj[] = { 15, 12, 9, 6, 3, 0, -3, -6, -9 };

    public final ArrayFunction knightAdjustmentMG;
    public final ArrayFunction knightAdjustmentEG;
    public ArrayFunction knightAdjustmentMGEG;

    public final ArrayFunction rookAdjustmentMG;
    public final ArrayFunction rookAdjustmentEG;
    public ArrayFunction rookAdjustmentMGEG;

    public ParameterizedAdjustmentsEvaluation(EvalConfig config) {
        tempoScore = readCombinedConfigVal(config, TEMPO_MG, TEMPO_EG, val -> tempoMgEg = val);

        bishopPairScore = readCombinedConfigVal(config, BISHOP_PAIR_MG, BISHOP_PAIR_EG, val -> bishopPairMgEg = val);
        knightPairScore = readCombinedConfigVal(config, KNIGHT_PAIR_MG, KNIGHT_PAIR_EG, val -> knightPairMgEg = val);
        rookPairScore = readCombinedConfigVal(config, ROOK_PAIR_MG, ROOK_PAIR_EG, val -> rookPairMgEg = val);

        knightAdjustmentMG = config.parseArray(KNIGHT_ADJ_MG);
        knightAdjustmentEG = config.parseArray(KNIGHT_ADJ_EG);

        rookAdjustmentMG = config.parseArray(ROOK_ADJ_MG);
        rookAdjustmentEG = config.parseArray(ROOK_ADJ_EG);

        updateCombinedVals();
    }

    public void updateCombinedVals() {
        knightAdjustmentMGEG = combine(knightAdjustmentMG, knightAdjustmentEG);
        rookAdjustmentMGEG = combine(rookAdjustmentMG, rookAdjustmentEG);
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
