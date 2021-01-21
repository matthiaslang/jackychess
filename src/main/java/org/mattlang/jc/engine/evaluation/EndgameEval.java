package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.evaltables.EndGame;

/**
 * Experimental Material Evaluation.
 * <p>
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class EndgameEval implements EvaluateFunction {

    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);

        int pattCheck = PattChecking.pattCheck(wstats, bstats, who2Move);
        if (pattCheck == -PATT_WEIGHT || pattCheck == PATT_WEIGHT) {
            return pattCheck;
        }

        int score = Evaluations.materialEval(currBoard, who2Move, true);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        score += COMMON_MOBILITY_WEIGHT * (wstats.mobility - bstats.mobility) * who2mov +
                COMMON_CAPTURABILITY_WEIGHT * (wstats.captures - bstats.captures) * who2mov;

        // apply opening patterns:
        score += EndGame.endgamePatterns(currBoard, who2Move);

        return score;

    }



}
