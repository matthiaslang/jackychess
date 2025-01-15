package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.mattlang.jc.board.BB.*;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPawnEvaluation.calcBlockedWhitePawns;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.Getter;

/**
 * Complexity Evaluation.
 */
@Getter
@EvalConfigurable(prefix = "complexity")
public class ParameterizedComplexityEvaluation implements EvalComponent {

    @EvalConfigParam
    private MgEgArrayFunction closednessKnightAdjustment;

    @EvalConfigParam
    private MgEgArrayFunction closednessRookAdjustment;

    @EvalConfigParam
    private int complexityTotalPawnsEG;

    @EvalConfigParam
    private int complexityPawnFlanksEG;

    @EvalConfigParam
    private int complexityPawnEndgameEG;

    @EvalConfigParam
    private int complexityAdjustmentEG;

    public ParameterizedComplexityEvaluation() {

    }

    @Override
    public void eval(EvalResult result, BoardRepresentation board) {
        // evaluate complexity related to given current eg eval value:
        result.getMgEgScore().addEg(evaluateComplexity(board, result.getMgEgScore().getEgScore()));
        result.getMgEgScore().add(evaluateClosedness(board));
    }

    int evaluateComplexity(BoardRepresentation board, int eg) {

        // Adjust endgame evaluation based on features related to how
        // likely the stronger side is to convert the position.
        // More often than not, this is a penalty for drawish positions.
        int sign = (eg > 0) ? 1 : -1;

        long pawns = board.getBoard().getPieceSet(FT_PAWN);
        boolean pawnsOnBothFlanks = (pawns & LEFT_FLANK) != 0
                && (pawns & RIGHT_FLANK) != 0;

        long knights = board.getBoard().getPieceSet(FT_KNIGHT);
        long bishops = board.getBoard().getPieceSet(FT_BISHOP);
        long rooks = board.getBoard().getPieceSet(FT_ROOK);
        long queens = board.getBoard().getPieceSet(FT_QUEEN);

        // Compute the initiative bonus or malus for the attacking side
        int complexity = complexityTotalPawnsEG * bitCount(pawns)
                + complexityPawnFlanksEG * (pawnsOnBothFlanks ? 1 : 0)
                + complexityPawnEndgameEG * ((knights | bishops | rooks | queens) == 0 ? 1 : 0)
                + complexityAdjustmentEG;

        // Avoid changing which side has the advantage
        return sign * max(complexity, -Math.abs(eg));
    }

    int evaluateClosedness(BoardRepresentation board) {

        int eval = 0;

        long white = board.getBoard().getColorMask(nWhite);
        long black = board.getBoard().getColorMask(nBlack);

        long knights = board.getBoard().getPieceSet(FT_KNIGHT);
        long rooks = board.getBoard().getPieceSet(FT_ROOK);
        long pawns = board.getBoard().getPieceSet(FT_PAWN);
        long whitePawns = white & pawns;
        long blackPawns = black & pawns;

        // Compute Closedness factor for this position
        int closedness = bitCount(pawns)
                + 3 * bitCount(calcBlockedWhitePawns(whitePawns, blackPawns))
                - 4 * openFileCount(pawns);
        closedness = max(0, min(8, closedness / 3));

        // Evaluate Knights based on how Closed the position is
        int count = bitCount(white & knights) - bitCount(black & knights);
        eval += count * closednessKnightAdjustment.calc(closedness);

        // Evaluate Rooks based on how Closed the position is
        count = bitCount(white & rooks) - bitCount(black & rooks);
        eval += count * closednessRookAdjustment.calc(closedness);

        return eval;
    }

}
