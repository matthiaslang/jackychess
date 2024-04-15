package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

import lombok.Getter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@Getter
@EvalConfigurable(prefix = "pst")
public class ParameterizedPstEvaluation implements EvalComponent {

    @EvalConfigParam(name = "pawn", mgEgCombined = true)
    private Pattern pawnMGEG;
    @EvalConfigParam(name = "knight", mgEgCombined = true)
    private Pattern knightMGEG;
    @EvalConfigParam(name = "bishop", mgEgCombined = true)
    private Pattern bishopMGEG;
    @EvalConfigParam(name = "rook", mgEgCombined = true)
    private Pattern rookMGEG;
    @EvalConfigParam(name = "queen", mgEgCombined = true)
    private Pattern queenMGEG;
    @EvalConfigParam(name = "king", mgEgCombined = true)
    private Pattern kingMGEG;

    @EvalConfigParam(name = "pawnMG", disableTuning = true)
    private Pattern pawnDeltaScoring;
    @EvalConfigParam(name = "knightMG", disableTuning = true)
    private Pattern knightDeltaScoring;
    @EvalConfigParam(name = "bishopMG", disableTuning = true)
    private Pattern bishopDeltaScoring;
    @EvalConfigParam(name = "rookMG", disableTuning = true)
    private Pattern rookDeltaScoring;
    @EvalConfigParam(name = "queenMG", disableTuning = true)
    private Pattern queenDeltaScoring;
    @EvalConfigParam(name = "kingMG", disableTuning = true)
    private Pattern kingDeltaScoring;

    public ParameterizedPstEvaluation(String subPath) {

    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        BitChessBoard bb = bitBoard.getBoard();

        result.getMgEgScore().add(pawnMGEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack)) +
                knightMGEG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack)) +
                bishopMGEG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack)) +
                rookMGEG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack)) +
                queenMGEG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack)) +
                kingMGEG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack)));
    }

    public int calcPstDelta(Color color, Move m) {
        Pattern pattern = getPstForFigure(m.getFigureType());
        return pattern.calcPstDelta(m.getToIndex(), m.getFromIndex(), color);
    }

    public Pattern getPstForFigure(byte figureType) {
        switch (figureType) {
        case FT_PAWN:
            return pawnDeltaScoring;
        case FT_KNIGHT:
            return knightDeltaScoring;
        case FT_BISHOP:
            return bishopDeltaScoring;
        case FT_ROOK:
            return rookDeltaScoring;
        case FT_QUEEN:
            return queenDeltaScoring;
        case FT_KING:
            return kingDeltaScoring;
        }
        throw new IllegalArgumentException("illegal figure type " + figureType);
    }

}
