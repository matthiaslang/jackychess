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

    private boolean deactivated = false;

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

        /**
         * some configs might not use material properties, but use only PST for the material evaluation.
         * In that case the properties are all 0, and we can disable this evaluation.
         */
        deactivated =
                pawnMG + knightMG + bishopMG + rookMG + queenMG + pawnEG + knightEG + bishopEG + rookEG + queenEG == 0;
    }

    @Override
    public void eval(EvalResult result, BitBoard bitBoard, Color who2Move) {
        if (deactivated) {
            return;
        }
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        BitChessBoard bb = bitBoard.getBoard();

        int pawnsDiff = bb.getPawnsCount(nWhite) - bb.getPawnsCount(nBlack);
        int knightsDiff = bb.getKnightsCount(nWhite) - bb.getKnightsCount(nBlack);
        int bishopsDiff = bb.getBishopsCount(nWhite) - bb.getBishopsCount(nBlack);
        int rooksDiff = bb.getRooksCount(nWhite) - bb.getRooksCount(nBlack);
        int queensDiff = bb.getQueensCount(nWhite) - bb.getQueensCount(nBlack);

        result.midGame += pawnMG * pawnsDiff * who2mov +
                knightMG * knightsDiff * who2mov +
                bishopMG * bishopsDiff * who2mov +
                rookMG * rooksDiff * who2mov +
                queenMG * queensDiff * who2mov;

        result.endGame += pawnEG * pawnsDiff * who2mov +
                knightEG * knightsDiff * who2mov +
                bishopEG * bishopsDiff * who2mov +
                rookEG * rooksDiff * who2mov +
                queenEG * queensDiff * who2mov;
    }
}
