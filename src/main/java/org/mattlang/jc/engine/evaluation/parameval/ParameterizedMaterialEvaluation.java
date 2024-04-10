package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;

import lombok.Getter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@EvalConfigurable(prefix = "mat")
@EvalValueInterval(min = 0, max = 2000)
public class ParameterizedMaterialEvaluation implements EvalComponent {

    @EvalConfigParam(configName = "PawnEG", disableTuning = true)
    @Getter
    private int pawnEG;
    @EvalConfigParam(configName = "KnightEG", disableTuning = true)
    private int knightEG;
    @EvalConfigParam(configName = "BishopEG", disableTuning = true)
    private int bishopEG;
    @EvalConfigParam(configName = "RookEG", disableTuning = true)
    @Getter
    private int rookEG;
    @EvalConfigParam(configName = "QueenEG", disableTuning = true)
    @Getter
    private int queenEG;

    @EvalConfigParam(configName = "Pawn", mgEgCombined = true)
    private int pawnMGEG;
    @EvalConfigParam(configName = "Knight", mgEgCombined = true)
    private int knightMGEG;
    @EvalConfigParam(configName = "Bishop", mgEgCombined = true)
    private int bishopMGEG;
    @EvalConfigParam(configName = "Rook", mgEgCombined = true)
    private int rookMGEG;
    @EvalConfigParam(configName = "Queen", mgEgCombined = true)
    private int queenMGEG;

    public ParameterizedMaterialEvaluation(boolean forTuning, EvalConfig config) {
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

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

}
