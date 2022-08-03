package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.parameval.endgame.EndGameRules;
import org.mattlang.jc.engine.tt.IntIntCache;
import org.mattlang.jc.material.Material;

import lombok.Getter;

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

    @Getter
    private ParameterizedMaterialEvaluation matEvaluation;

    @Getter
    private ParameterizedPstEvaluation pstEvaluation;

    private ParameterizedMobilityEvaluation mobEvaluation;

    private ParameterizedPawnEvaluation pawnEvaluation;

    ParameterizedMaterialCorrectionEvaluation matCorrection;

    ParameterizedAdjustmentsEvaluation adjustments;

    private EvalResult result = new EvalResult();

    private boolean caching = false;

    private boolean endgameEvaluations = false;

    private IntIntCache evalCache = EvalCache.instance;

    public ParameterizedEvaluation() {

        EvalConfig config = new EvalConfig();

        caching = config.getBoolProp("caching.active");
        endgameEvaluations = config.getBoolProp("endgameEvaluations.active");

        matEvaluation = new ParameterizedMaterialEvaluation(config);
        pstEvaluation = new ParameterizedPstEvaluation(config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(config);
        pawnEvaluation = new ParameterizedPawnEvaluation(config);

        matCorrection = new ParameterizedMaterialCorrectionEvaluation(config);
        adjustments = new ParameterizedAdjustmentsEvaluation(config);
    }

    /**
     * Creates a parameterized evaluation for tuning: disabled cache and disabled end game functions.
     *
     * @return
     */
    public static ParameterizedEvaluation createForTuning() {
        //
        ParameterizedEvaluation eval = new ParameterizedEvaluation();
        // disable caching for tuning since the parameters change during tuning:
        eval.caching = false;
        // disable special end game functions, as they get not tuned (because they do not have any parameters)
        eval.endgameEvaluations = false;
        return eval;
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        if (caching) {
            int cachedResult = evalCache.find(currBoard.getZobristHash());
            if (cachedResult != IntIntCache.NORESULT) {
                return cachedResult;
            }
        }

        result.clear();

        matEvaluation.eval(result, currBoard);

        if (endgameEvaluations) {
            EndGameRules endGameRule = matchesRule(currBoard, result.endGame);
            if (endGameRule != null) {

                Color stronger = result.endGame > 0 ? Color.WHITE : Color.BLACK;
                Color weaker = stronger.invert();

                int score = endGameRule.getEndgameFunction().evaluate(currBoard, stronger, weaker, matEvaluation);
                if (caching) {
                    evalCache.save(currBoard.getZobristHash(), score);
                }
                return score;
            }
        }

        pstEvaluation.eval(result, currBoard);
        mobEvaluation.eval(result, currBoard);
        pawnEvaluation.eval(result, currBoard);
        result.result += adjustments.adjust(currBoard.getBoard(), who2Move);

        int score = result.calcCompleteScore(currBoard);

        score = matCorrection.correct(currBoard, score);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        score = score * who2mov;

        if (caching) {
            evalCache.save(currBoard.getZobristHash(), score);
        }

        return score;
    }



    private EndGameRules matchesRule(BoardRepresentation board, int materialScore) {
        long figs = board.getBoard().getColorMask(nWhite) | board.getBoard()
                .getColorMask(nBlack);
        if (Long.bitCount(figs) <= 5) {
            Material matWeaker = new Material();
            Material matStronger = new Material();

            Material currMaterial = board.getMaterial();
            if (materialScore > 0) {
                // white is stronger
                matStronger.setMaterial(currMaterial.getWhiteMat());
                matWeaker.setMaterial(currMaterial.getBlackAsWhitePart());
            } else {
                // black is stronger
                matStronger.setMaterial(currMaterial.getBlackAsWhitePart());
                matWeaker.setMaterial(currMaterial.getWhiteMat());
            }

            return EndGameRules.findRule(matStronger, matWeaker);

        }

        return null;

    }

    /**
     * Used in tuning: Returns true if the evaluation would use a special end game function for that position.
     * @param currBoard
     * @return
     */
    public boolean isUsingEndgameFunction(BoardRepresentation currBoard) {
        result.clear();
        matEvaluation.eval(result, currBoard);
        EndGameRules endGameRule = matchesRule(currBoard, result.endGame);
        return endGameRule != null;
    }
}
