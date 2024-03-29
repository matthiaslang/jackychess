package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@Getter
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

    private int pawnMGEG;
    private int knightMGEG;
    private int bishopMGEG;
    private int rookMGEG;
    private int queenMGEG;

    private boolean deactivated = false;

    public ParameterizedMaterialEvaluation(boolean forTuning, EvalConfig config) {

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

        pawnMGEG = MgEgScore.createMgEgScore(pawnMG, pawnEG);
        knightMGEG = MgEgScore.createMgEgScore(knightMG, knightEG);
        bishopMGEG = MgEgScore.createMgEgScore(bishopMG, bishopEG);
        rookMGEG = MgEgScore.createMgEgScore(rookMG, rookEG);
        queenMGEG = MgEgScore.createMgEgScore(queenMG, queenEG);

        /**
         * some configs might not use material properties, but use only PST for the material evaluation.
         * In that case the properties are all 0, and we can disable this evaluation.
         */
        deactivated = !forTuning &&
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

        result.getMgEgScore().add(pawnMGEG * pawnsDiff +
                knightMGEG * knightsDiff +
                bishopMGEG * bishopsDiff +
                rookMGEG * rooksDiff +
                queenMGEG * queensDiff);
    }

    public int evalEndGameMaterialOfSide(BoardRepresentation bitBoard, Color color) {
        BitChessBoard bb = bitBoard.getBoard();

        return pawnEG * bb.getPawnsCount(color) +
                knightEG * bb.getKnightsCount(color) +
                bishopEG * bb.getBishopsCount(color) +
                rookEG * bb.getRooksCount(color) +
                queenEG * bb.getQueensCount(color);
    }

    public void setPawnMG(int pawnMG) {
        this.pawnMG = pawnMG;
        pawnMGEG = MgEgScore.createMgEgScore(pawnMG, pawnEG);
    }

    public void setKnightMG(int knightMG) {
        this.knightMG = knightMG;
        knightMGEG = MgEgScore.createMgEgScore(knightMG, knightEG);
    }

    public void setBishopMG(int bishopMG) {
        this.bishopMG = bishopMG;
        bishopMGEG = MgEgScore.createMgEgScore(bishopMG, bishopEG);
    }

    public void setRookMG(int rookMG) {
        this.rookMG = rookMG;
        rookMGEG = MgEgScore.createMgEgScore(rookMG, rookEG);
    }

    public void setQueenMG(int queenMG) {
        this.queenMG = queenMG;
        queenMGEG = MgEgScore.createMgEgScore(queenMG, queenEG);
    }

    public void setPawnEG(int pawnEG) {
        this.pawnEG = pawnEG;
        pawnMGEG = MgEgScore.createMgEgScore(pawnMG, pawnEG);
    }

    public void setKnightEG(int knightEG) {
        this.knightEG = knightEG;
        knightMGEG = MgEgScore.createMgEgScore(knightMG, knightEG);
    }

    public void setBishopEG(int bishopEG) {
        this.bishopEG = bishopEG;
        bishopMGEG = MgEgScore.createMgEgScore(bishopMG, bishopEG);
    }

    public void setRookEG(int rookEG) {
        this.rookEG = rookEG;
        rookMGEG = MgEgScore.createMgEgScore(rookMG, rookEG);
    }

    public void setQueenEG(int queenEG) {
        this.queenEG = queenEG;
        queenMGEG = MgEgScore.createMgEgScore(queenMG, queenEG);
    }
}
