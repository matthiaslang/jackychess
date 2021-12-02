package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.parameval.ConfigTools.loadPropertyFile;

import java.util.Properties;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;

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

    private String configName = "default";

    private ParameterizedMaterialEvaluation matEvaluation;

    private ParameterizedPstEvaluation pstEvaluation;

    private ParameterizedMobilityEvaluation mobEvaluation;

    private EvalResult result = new EvalResult();

    public ParameterizedEvaluation() {
        configName = Factory.getDefaults().getConfig().evaluateParamSet.getValue().name().toLowerCase();

        // read in all configuration for all the evaluation components:

        String configDir = "/config/" + configName + "/";

        Properties properties = loadPropertyFile(configDir + "config.properties");

        matEvaluation = new ParameterizedMaterialEvaluation(properties);
        pstEvaluation = new ParameterizedPstEvaluation(configDir + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(properties);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        BitBoard bitBoard = (BitBoard) currBoard;

        result.midGame = 0;
        result.endGame = 0;

        matEvaluation.eval(result, bitBoard, who2Move);
        pstEvaluation.eval(result, bitBoard, who2Move);
        mobEvaluation.eval(result, bitBoard, who2Move);

        double score = PhaseCalculator.scaleByPhase(bitBoard, result.midGame, result.endGame);
        return (int) score;
    }
}
