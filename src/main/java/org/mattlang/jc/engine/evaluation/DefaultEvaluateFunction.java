package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.taperedEval.*;

/**
 * Experimental Material Evaluation evaluating pawn structure and king safety.
 * <p>
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class DefaultEvaluateFunction implements EvaluateFunction {

    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    private CommonEval commonEval = new CommonEval();

    private PawnStructureEval pawnStructureEval = new PawnStructureEval();

    private EndGameMaterialEval endGameMaterialEval = new EndGameMaterialEval();

    private KingSafetyEval kingSafetyEval = new KingSafetyEval();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);

        int pattCheck = PattChecking.pattCheck(wstats, bstats, who2Move);
        if (pattCheck == -PATT_WEIGHT || pattCheck == PATT_WEIGHT) {
            return pattCheck;
        }

        int score = commonEval.eval(currBoard, wstats, bstats, who2Move);

        EvalStats evalStats = new EvalStats(currBoard);

        score+=pawnStructureEval.eval(evalStats, who2Move);

        score+=endGameMaterialEval.eval(evalStats, who2Move);

        score+=kingSafetyEval.eval(currBoard, evalStats, who2Move);

        return score;
    }

}
