package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import java.util.Properties;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
public class ParameterizedMaterialEvaluation implements EvalComponent {

    private int pawnMG;
    private int knightMG;
    private int bishopMG;
    private int rookMG;
    private int queenMG;

    private int pawnEG;
    private int knightEG;
    private int bishopEG;
    private int rookEG;
    private int queenEG;

    public ParameterizedMaterialEvaluation(Properties properties) {

        pawnMG = ConfigTools.getIntProp(properties, "matPawnMG");
        knightMG = ConfigTools.getIntProp(properties, "matKnightMG");
        bishopMG = ConfigTools.getIntProp(properties, "matBishopMG");
        rookMG = ConfigTools.getIntProp(properties, "matRookMG");
        queenMG = ConfigTools.getIntProp(properties, "matQueenMG");

        pawnEG = ConfigTools.getIntProp(properties, "matPawnEG");
        knightEG = ConfigTools.getIntProp(properties, "matKnightEG");
        bishopEG = ConfigTools.getIntProp(properties, "matBishopEG");
        rookEG = ConfigTools.getIntProp(properties, "matRookEG");
        queenEG = ConfigTools.getIntProp(properties, "matQueenEG");
    }

    @Override
    public void eval(EvalResult result, BitBoard bitBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        BitChessBoard bb = bitBoard.getBoard();

        result.midGame += pawnMG * (bb.getPawnsCount(nWhite) - bb.getPawnsCount(nBlack)) * who2mov +
                knightMG * (bb.getKnightsCount(nWhite) - bb.getKnightsCount(nBlack)) * who2mov +
                bishopMG * (bb.getBishopsCount(nWhite) - bb.getBishopsCount(nBlack)) * who2mov +
                rookMG * (bb.getRooksCount(nWhite) - bb.getRooksCount(nBlack)) * who2mov +
                queenMG * (bb.getQueensCount(nWhite) - bb.getQueensCount(nBlack)) * who2mov;

        result.endGame += pawnEG * (bb.getPawnsCount(nWhite) - bb.getPawnsCount(nBlack)) * who2mov +
                knightEG * (bb.getKnightsCount(nWhite) - bb.getKnights(nBlack)) * who2mov +
                bishopEG * (bb.getBishopsCount(nWhite) - bb.getBishopsCount(nBlack)) * who2mov +
                rookEG * (bb.getRooksCount(nWhite) - bb.getRooksCount(nBlack)) * who2mov +
                queenEG * (bb.getQueensCount(nWhite) - bb.getQueensCount(nBlack)) * who2mov;
    }
}
