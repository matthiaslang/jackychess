package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.tt.IntCache;

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

    ParameterizedAdjustmentsEvaluation adjustments;

    private EvalResult result = new EvalResult();

    private boolean caching=false;

    private IntCache evalCache = EvalCache.instance;



    public ParameterizedEvaluation() {

        EvalConfig config = new EvalConfig();

        caching = config.getBoolProp("caching.active");

        matEvaluation = new ParameterizedMaterialEvaluation(config);
        pstEvaluation = new ParameterizedPstEvaluation(config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(config);
        pawnEvaluation = new ParameterizedPawnEvaluation(config);

        matCorrection = new ParameterizedMaterialCorrectionEvaluation(config);
        adjustments = new ParameterizedAdjustmentsEvaluation(config);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        if (caching) {
            int cachedResult = evalCache.find(currBoard.getZobristHash());
            if (cachedResult != IntCache.NORESULT) {
                return cachedResult;
            }
        }

        result.clear();

        matEvaluation.eval(result, currBoard);
        pstEvaluation.eval(result, currBoard);
        mobEvaluation.eval(result, currBoard);
        pawnEvaluation.eval(result, currBoard);
        result.result += adjustments.adjust(currBoard.getBoard(), who2Move);

        int score = result.calcCompleteScore(currBoard);

        score = matCorrection.correct(currBoard, score);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        score = score * who2mov;

        if (caching){
            evalCache.save(currBoard.getZobristHash(), score);
        }

        return score;
    }
}
