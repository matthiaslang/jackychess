package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitBoard.MAXMOVES;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.IncrementalEvaluateFunction;
import org.mattlang.jc.engine.evaluation.parameval.endgame.EndGameRules;
import org.mattlang.jc.engine.tt.IntIntCache;
import org.mattlang.jc.material.Material;

/**
 * Another experimental evaluation.
 * This evaluation is parametrized as much as possible with parameters read from resource files.
 * Reason is to make different configurations during testing and optimization.
 *
 * The evaluation has different components which can all be configured via resource files.
 * All parameters are bundled by a named "configuration" which can be selected via UCI (or property).
 * All parameters of a bundled configuration are inside a resource folder with the same name as the configuration.
 */
public final class ParameterizedEvaluation implements EvaluateFunction, IncrementalEvaluateFunction {

    private ParameterizedMaterialEvaluation matEvaluation;

    private ParameterizedPstEvaluation pstEvaluation;

    private ParameterizedMobilityEvaluation mobEvaluation;

    private ParameterizedPawnEvaluation pawnEvaluation;

    ParameterizedMaterialCorrectionEvaluation matCorrection;

    ParameterizedAdjustmentsEvaluation adjustments;

    private EvalResult result = new EvalResult();

    private boolean caching = false;

    private boolean endgameEvaluations = false;

    private IntIntCache evalCache = EvalCache.instance;
    private BoardRepresentation associatedIncrementalBoard;

    private int[] historyIncMGEval = new int[MAXMOVES];
    private int[] historyIncEGEval = new int[MAXMOVES];

    public ParameterizedEvaluation() {

        EvalConfig config = new EvalConfig();

        caching = config.getBoolProp("caching.active");
        endgameEvaluations = config.getBoolProp("endgameEvaluations.active");

        matEvaluation = new ParameterizedMaterialEvaluation(config);
        pstEvaluation = new ParameterizedPstEvaluation(matEvaluation, config.getConfigDir() + "pst/");

        mobEvaluation = new ParameterizedMobilityEvaluation(config);
        pawnEvaluation = new ParameterizedPawnEvaluation(config);

        matCorrection = new ParameterizedMaterialCorrectionEvaluation(config);
        adjustments = new ParameterizedAdjustmentsEvaluation(config);
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

        mobEvaluation.eval(result, currBoard);
        pawnEvaluation.eval(result, currBoard);

        if (associatedIncrementalBoard == currBoard) {
            result.add(incrementalResult);
        } else {
            pstEvaluation.eval(result, currBoard);
        }

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

    private EvalResult incrementalResult = new EvalResult();

    @Override
    public void initIncrementalValues(BoardRepresentation board) {
        this.associatedIncrementalBoard = board;
        incrementalResult.clear();
        pstEvaluation.eval(incrementalResult, board);
    }

    @Override
    public void removeFigure(int pos, byte figCode) {
        pstEvaluation.incrementalRemoveFigure(incrementalResult, pos, figCode);
    }

    @Override
    public void addFigure(int pos, byte figCode) {
        pstEvaluation.incrementalAddFigure(incrementalResult, pos, figCode);
    }

    @Override
    public void moveFigure(int from, int to, byte figCode) {
        pstEvaluation.incrementalMoveFigure(incrementalResult, from, to, figCode);
    }

    @Override
    public void unregisterIncrementalEval() {
        this.associatedIncrementalBoard = null;
    }

    @Override
    public void pop(int moveCounter) {
        incrementalResult.midGame = historyIncMGEval[moveCounter];
        incrementalResult.endGame = historyIncEGEval[moveCounter];
    }

    @Override
    public void push(int moveCounter) {
        historyIncMGEval[moveCounter] = incrementalResult.midGame;
        historyIncEGEval[moveCounter] = incrementalResult.endGame;
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

}
