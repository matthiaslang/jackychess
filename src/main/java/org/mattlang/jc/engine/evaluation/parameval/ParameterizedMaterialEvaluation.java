package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;
import lombok.Setter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@Getter
@Setter
public class ParameterizedMaterialEvaluation implements EvalComponent {

    public static final String MAT_PAWN_MG = "matPawnMG";
    public static final String MAT_KNIGHT_MG = "matKnightMG";
    public static final String MAT_BISHOP_MG = "matBishopMG";
    public static final String MAT_ROOK_MG = "matRookMG";
    public static final String MAT_QUEEN_MG = "matQueenMG";
    public static final String MAT_PAWN_EG = "matPawnEG";
    public static final String MAT_KNIGHT_EG = "matKnightEG";
    public static final String MAT_BISHOP_EG = "matBishopEG";
    public static final String MAT_ROOK_EG = "matRookEG";
    public static final String MAT_QUEEN_EG = "matQueenEG";
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

        pawnMG = config.getIntProp(MAT_PAWN_MG);
        knightMG = config.getIntProp(MAT_KNIGHT_MG);
        bishopMG = config.getIntProp(MAT_BISHOP_MG);
        rookMG = config.getIntProp(MAT_ROOK_MG);
        queenMG = config.getIntProp(MAT_QUEEN_MG);

        pawnEG = config.getIntProp(MAT_PAWN_EG);
        knightEG = config.getIntProp(MAT_KNIGHT_EG);
        bishopEG = config.getIntProp(MAT_BISHOP_EG);
        rookEG = config.getIntProp(MAT_ROOK_EG);
        queenEG = config.getIntProp(MAT_QUEEN_EG);

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

    public int evalEndGameMaterialOfSide(BoardRepresentation bitBoard, Color color) {
        BitChessBoard bb = bitBoard.getBoard();

        return pawnEG * bb.getPawnsCount(color) +
                knightEG * bb.getKnightsCount(color) +
                bishopEG * bb.getBishopsCount(color) +
                rookEG * bb.getRooksCount(color) +
                queenEG * bb.getQueensCount(color);
    }
}
