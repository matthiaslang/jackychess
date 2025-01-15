package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;

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

    @EvalConfigParam(disableTuning = true)
    @Getter
    private int pawnEG;
    @EvalConfigParam(disableTuning = true)
    private int knightEG;
    @EvalConfigParam(disableTuning = true)
    private int bishopEG;
    @EvalConfigParam(disableTuning = true)
    @Getter
    private int rookEG;
    @EvalConfigParam(disableTuning = true)
    @Getter
    private int queenEG;

    @EvalConfigParam(mgEgCombined = true)
    private int pawn;
    @EvalConfigParam(mgEgCombined = true)
    private int knight;
    @EvalConfigParam(mgEgCombined = true)
    private int bishop;
    @EvalConfigParam(mgEgCombined = true)
    private int rook;
    @EvalConfigParam(mgEgCombined = true)
    private int queen;

    public ParameterizedMaterialEvaluation() {
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        BitChessBoard bb = bitBoard.getBoard();

        int pawnsDiff = bb.getPawnsCount(nWhite) - bb.getPawnsCount(nBlack);
        int knightsDiff = bb.getKnightsCount(nWhite) - bb.getKnightsCount(nBlack);
        int bishopsDiff = bb.getBishopsCount(nWhite) - bb.getBishopsCount(nBlack);
        int rooksDiff = bb.getRooksCount(nWhite) - bb.getRooksCount(nBlack);
        int queensDiff = bb.getQueensCount(nWhite) - bb.getQueensCount(nBlack);

        result.getMgEgScore().add(pawn * pawnsDiff +
                knight * knightsDiff +
                bishop * bishopsDiff +
                rook * rooksDiff +
                queen * queensDiff);
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
