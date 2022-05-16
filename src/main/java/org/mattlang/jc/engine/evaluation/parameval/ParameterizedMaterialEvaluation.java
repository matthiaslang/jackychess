package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;

/**
 * A Tapered, parameterized material evaluation.
 * Actually the values are added to the pst evaluation for performance reasons to not separately evaluate material
 * values.
 */
@Getter
public class ParameterizedMaterialEvaluation {

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
