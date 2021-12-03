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

    private EvalResult result = new EvalResult();

    public ParameterizedEvaluation() {

        EvalConfig config = new EvalConfig();

        matEvaluation = new ParameterizedMaterialEvaluation(config);
        pstEvaluation = new ParameterizedPstEvaluation(config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(config);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        BitBoard bitBoard = (BitBoard) currBoard;

        result.clear();

        matEvaluation.eval(result, bitBoard, who2Move);
        pstEvaluation.eval(result, bitBoard, who2Move);
        mobEvaluation.eval(result, bitBoard, who2Move);

        double score = scaleByPhase(bitBoard, result.midGame, result.endGame) + result.result;
        return (int) score;
    }
}
