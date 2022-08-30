package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;
import lombok.Setter;

/**
 * Paremeterized positional and material adjustments.
 */
public class ParameterizedAdjustmentsEvaluation {

    public static final String TEMPO = "tempo";
    public static final String BISHOP_PAIR = "bishopPair";
    public static final String KNIGHT_PAIR = "knightPair";
    public static final String ROOK_PAIR = "rookPair";
    @Getter
    @Setter
    private int tempo;
    @Getter
    @Setter
    private int bishopPair;
    @Getter
    @Setter
    private int knightPair;
    @Getter
    @Setter
    private int rookPair;

    /* adjustements of piece value based on the number of own pawns */
    int n_adj[] = { -20, -16, -12, -8, -4, 0, 4, 8, 12 };
    int r_adj[] = { 15, 12, 9, 6, 3, 0, -3, -6, -9 };

    public ParameterizedAdjustmentsEvaluation(EvalConfig config) {
        tempo = config.getIntProp(TEMPO);
        bishopPair = config.getIntProp(BISHOP_PAIR);
        knightPair = config.getIntProp(KNIGHT_PAIR);
        rookPair = config.getIntProp(ROOK_PAIR);
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

        adjWhite += n_adj[whitePawns] * whiteKnights;
        adjBlack += n_adj[blackPawns] * blackKnights;
        adjWhite += r_adj[whitePawns] * whiteRooks;
        adjBlack += r_adj[blackPawns] * blackRooks;

        return result + (adjWhite - adjBlack);
    }
}
