package org.mattlang.jc.engine.evaluation.pstEval2;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.taperedEval.EndGameMaterialEval;
import org.mattlang.jc.engine.evaluation.taperedEval.EvalStats;
import org.mattlang.jc.engine.evaluation.taperedEval.KingSafetyEval;
import org.mattlang.jc.engine.evaluation.taperedEval.PawnStructureEval;

/**
 * Another experimental evaluation.
 * It uses basically the minimal pst evaluation and
 * additionally some positional evaluations like
 *
 * - EndgameMaterial
 * - PawnStructure
 * - KingSafety
 */
public class PstEvaluation2 implements EvaluateFunction {

    private MinimalPstEvaluation pstEvaluation = new MinimalPstEvaluation();

    private EndGameMaterialEval endGameMaterialEval = new EndGameMaterialEval();

    private PawnStructureEval pawnStructureEval = new PawnStructureEval();

    private KingSafetyEval kingSafetyEval = new KingSafetyEval();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int score = pstEvaluation.eval(currBoard, who2Move);

        EvalStats evalStats = new EvalStats(currBoard);

//        score += pawnStructureEval.eval(evalStats, who2Move);

        score += endGameMaterialEval.eval(evalStats, who2Move);

//        score += kingSafetyEval.eval(currBoard, evalStats, who2Move);
        
        return score;
    }
}
