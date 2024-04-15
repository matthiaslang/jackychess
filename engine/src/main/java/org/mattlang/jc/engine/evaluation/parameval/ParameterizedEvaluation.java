package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurator;
import org.mattlang.jc.engine.evaluation.parameval.endgame.EndGameRules;
import org.mattlang.jc.engine.tt.IntIntCache;
import org.mattlang.jc.material.Material;

import lombok.Getter;
import lombok.Setter;

/**
 * Another experimental evaluation.
 * This evaluation is parametrized as much as possible with parameters read from resource files.
 * Reason is to make different configurations during testing and optimization.
 *
 * The evaluation has different components which can all be configured via resource files.
 * All parameters are bundled by a named "configuration" which can be selected via UCI (or property).
 * All parameters of a bundled configuration are inside a resource folder with the same name as the configuration.
 */
@EvalConfigurable
public class ParameterizedEvaluation implements EvaluateFunction {

    @Getter
    private ParameterizedMaterialEvaluation matEvaluation;

    @Getter
    private ParameterizedPstEvaluation pstEvaluation;

    @Getter
    private ParameterizedMobilityEvaluation mobEvaluation;

    @Getter
    private ParameterizedKingEvaluation kingEvaluation;

    @Getter
    private ParameterizedPawnEvaluation pawnEvaluation;

    @Getter
    private ParameterizedThreatsEvaluation threatsEvaluation;

    @Getter
    private ParameterizedComplexityEvaluation complexityEvaluation;

    private ParameterizedSpaceEvaluation spaceEvaluation;

    ParameterizedMaterialCorrectionEvaluation matCorrection;

    @Getter
    ParameterizedAdjustmentsEvaluation adjustments;

    private EvalResult result = new EvalResult();

    @Getter
    @Setter
    private boolean caching = false;

    private boolean endgameEvaluations = false;

    @Getter
    /**
     * set in tuning runs.
     */
    private boolean forTuning = false;

    private IntIntCache evalCache = EvalCache.instance;

    private PawnCache pawnCache = PawnCache.EMPTY_CACHE;

    public ParameterizedEvaluation() {
        this(false);
    }

    public ParameterizedEvaluation(boolean forTuning) {
        this(new EvalConfig(), forTuning);
    }

    public ParameterizedEvaluation(String configName, boolean forTuning) {
        this(new EvalConfig(configName), forTuning);
    }

    public ParameterizedEvaluation(EvalConfig config, boolean forTuning) {
        this.forTuning = forTuning;

        caching = config.getBoolProp("caching.active");
        endgameEvaluations = config.getBoolProp("endgameEvaluations.active");

        EvalConfigurator configurator = new EvalConfigurator(config);

        matEvaluation = new ParameterizedMaterialEvaluation(forTuning, config);
        pstEvaluation = new ParameterizedPstEvaluation(config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(forTuning, config);
        pawnEvaluation = new ParameterizedPawnEvaluation(forTuning, caching, config);

        matCorrection = new ParameterizedMaterialCorrectionEvaluation(config);
        adjustments = new ParameterizedAdjustmentsEvaluation(config);

        threatsEvaluation = new ParameterizedThreatsEvaluation(forTuning, config);

        spaceEvaluation = new ParameterizedSpaceEvaluation();

        kingEvaluation = new ParameterizedKingEvaluation(forTuning, config);
        complexityEvaluation = new ParameterizedComplexityEvaluation(forTuning, config);

        configurator.configure(this);
    }

    /**
     * Creates a parameterized evaluation for tuning: disabled cache and disabled end game functions.
     *
     * @return
     */
    public static ParameterizedEvaluation createForTuning() {
        //
        ParameterizedEvaluation eval = new ParameterizedEvaluation(true);
        // disable caching for tuning since the parameters change during tuning:
        eval.caching = false;
        // disable special end game functions, as they get not tuned (because they do not have any parameters)
        eval.endgameEvaluations = false;
        return eval;
    }

    public static ParameterizedEvaluation createForTuning(String startEvalConfig) {
        //
        ParameterizedEvaluation eval = new ParameterizedEvaluation(startEvalConfig, true);
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

        // do mat evaluation first to have material values used for end game rules to decide the stronger side
        matEvaluation.eval(result, currBoard);

        int materialScore = result.getMgEgScore().getEgScore();

        if (endgameEvaluations) {
            EndGameRules endGameRule = matchesRule(currBoard, materialScore);
            if (endGameRule != null) {

                Color stronger = materialScore > 0 ? Color.WHITE : Color.BLACK;
                Color weaker = stronger.invert();

                int score = endGameRule.getEndgameFunction().evaluate(currBoard, stronger, weaker, matEvaluation);
                if (caching) {
                    evalCache.save(currBoard.getZobristHash(), score);
                }
                return score;
            }
        }

        if (caching) {
            long pawnHashKey = currBoard.getPawnKingZobristHash();
            result.setPawnEntry(pawnCache.find(pawnHashKey));
        }

        pstEvaluation.eval(result, currBoard);
        // do mobility rel. early as it calculates attacks which are needed by some evaluations later on:
        mobEvaluation.eval(result, currBoard);
        pawnEvaluation.eval(result, currBoard);
        result.getMgEgScore().add(adjustments.adjust(currBoard.getBoard(), who2Move));

        threatsEvaluation.eval(result, currBoard);

        kingEvaluation.eval(result, currBoard);

        complexityEvaluation.eval(result, currBoard);

        spaceEvaluation.eval(result, currBoard);

        int score = result.calcCompleteScore(currBoard);

        score = matCorrection.correct(currBoard, score);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        score = score * who2mov;

        if (caching) {
            evalCache.save(currBoard.getZobristHash(), score);
            if (result.getPawnEntry() == null) {
                pawnCache.save(currBoard.getPawnKingZobristHash(), result);
            }
        }

        return score;
    }

    @Override
    public int calcPstDelta(Color color, Move m) {
        return pstEvaluation.calcPstDelta(color, m);
    }

    @Override
    public void setPawnCache(PawnCache pawnCache) {
        this.pawnCache = pawnCache;
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
     *
     * @param currBoard
     * @return
     */
    public boolean isUsingEndgameFunction(BoardRepresentation currBoard) {
        result.clear();
        matEvaluation.eval(result, currBoard);
        EndGameRules endGameRule = matchesRule(currBoard, result.getMgEgScore().getEgScore());
        return endGameRule != null;
    }
}
