package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.PhaseCalculator.scaleByPhase;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.EvaluateFunction;

/**
 * Another experimental evaluation.
 * This evaluation is parametrized as much as possible with parameters read from resource files.
 * Reason is to make different configurations during testing and optimization.
 *
 * The evaluation has different components which can all be configured via resource files.
 * All parameters are bundled by a named "configuration" which can be selected via UCI (or property).
 * All parameters of a bundled configuration are inside a resource folder with the same name as the configuration.
 */
public class ParameterizedEvaluation implements EvaluateFunction {

    private ParameterizedMaterialEvaluation matEvaluation;

    private ParameterizedPstEvaluation pstEvaluation;

    private ParameterizedMobilityEvaluation mobEvaluation;

    private ParameterizedPawnEvaluation pawnEvaluation;

    ParameterizedMaterialCorrectionEvaluation matCorrection;

    private EvalResult result = new EvalResult();

    public ParameterizedEvaluation() {

        EvalConfig config = new EvalConfig();

        matEvaluation = new ParameterizedMaterialEvaluation(config);
        pstEvaluation = new ParameterizedPstEvaluation(config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(config);
        pawnEvaluation = new ParameterizedPawnEvaluation(config);

        matCorrection = new ParameterizedMaterialCorrectionEvaluation(config);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        BitBoard bitBoard = (BitBoard) currBoard;

        result.clear();

        matEvaluation.eval(result, bitBoard);
        pstEvaluation.eval(result, bitBoard);
        mobEvaluation.eval(result, bitBoard);
        pawnEvaluation.eval(result, bitBoard);

        int score = (int) scaleByPhase(bitBoard, result.midGame, result.endGame) + result.result;

        score = matCorrection.correct(bitBoard.getBoard(), score);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        score = score * who2mov;
        return (int) score;
    }
}
