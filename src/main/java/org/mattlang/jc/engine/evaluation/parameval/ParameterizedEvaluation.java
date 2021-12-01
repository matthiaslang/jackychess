package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.Factory;
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

    private String configName = "default";

    private ParameterizedPstEvaluation pstEvaluation;

    public ParameterizedEvaluation() {
        configName = Factory.getDefaults().getConfig().evaluateParamSet.getValue().name().toLowerCase();

        // read in all configuration for all the evaluation components:

        pstEvaluation = new ParameterizedPstEvaluation("/config/" + configName + "/pst/");

    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        BitBoard bitBoard = (BitBoard) currBoard;

        int score = pstEvaluation.eval(currBoard, who2Move);

        return score;
    }
}
