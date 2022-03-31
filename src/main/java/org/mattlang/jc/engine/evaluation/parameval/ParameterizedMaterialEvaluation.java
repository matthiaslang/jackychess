package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@Getter
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

    public ParameterizedMaterialEvaluation(EvalConfig config) {

        pawnMG = config.getIntProp("matPawnMG");
        knightMG = config.getIntProp("matKnightMG");
        bishopMG = config.getIntProp("matBishopMG");
        rookMG = config.getIntProp("matRookMG");
        queenMG = config.getIntProp("matQueenMG");

        pawnEG = config.getIntProp("matPawnEG");
        knightEG = config.getIntProp("matKnightEG");
        bishopEG = config.getIntProp("matBishopEG");
        rookEG = config.getIntProp("matRookEG");
        queenEG = config.getIntProp("matQueenEG");

        /**
         * some configs might not use material properties, but use only PST for the material evaluation.
         * In that case the properties are all 0, and we can disable this evaluation.
         */
        deactivated =
                pawnMG + knightMG + bishopMG + rookMG + queenMG + pawnEG + knightEG + bishopEG + rookEG + queenEG == 0;
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        if (deactivated) {
            return;
        }
        BitChessBoard bb = bitBoard.getBoard();

        int pawnsDiff = bb.getPawnsCount(nWhite) - bb.getPawnsCount(nBlack);
        int knightsDiff = bb.getKnightsCount(nWhite) - bb.getKnightsCount(nBlack);
        int bishopsDiff = bb.getBishopsCount(nWhite) - bb.getBishopsCount(nBlack);
        int rooksDiff = bb.getRooksCount(nWhite) - bb.getRooksCount(nBlack);
        int queensDiff = bb.getQueensCount(nWhite) - bb.getQueensCount(nBlack);

        result.midGame += pawnMG * pawnsDiff +
                knightMG * knightsDiff +
                bishopMG * bishopsDiff +
                rookMG * rooksDiff +
                queenMG * queensDiff;

        result.endGame += pawnEG * pawnsDiff +
                knightEG * knightsDiff +
                bishopEG * bishopsDiff +
                rookEG * rooksDiff +
                queenEG * queensDiff;
    }

    public int evalEndGameMaterialOfSide(BoardRepresentation bitBoard, int color) {
        BitChessBoard bb = bitBoard.getBoard();

        return pawnEG * bb.getPawnsCount(color) +
                knightEG * bb.getKnightsCount(color) +
                bishopEG * bb.getBishopsCount(color) +
                rookEG * bb.getRooksCount(color) +
                queenEG * bb.getQueensCount(color);
    }
}
