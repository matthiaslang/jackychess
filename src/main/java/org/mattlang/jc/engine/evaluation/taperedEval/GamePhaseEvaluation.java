package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.PATT_WEIGHT;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.BoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.PattChecking;

/**
 * Tapered Evaluation function with special opening and endgame evaluate function.
 */
public class GamePhaseEvaluation implements EvaluateFunction {

    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    private OpeningEval openingEval = new OpeningEval();
    private EndgameEval endgameEval = new EndgameEval();
    private CommonEval commonEval = new CommonEval();

    private TaperedEval taperedEval = new TaperedEval();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);

        int pattCheck = PattChecking.pattCheck(wstats, bstats, who2Move);
        if (pattCheck == -PATT_WEIGHT || pattCheck == PATT_WEIGHT) {
            return pattCheck;
        }


        int common = commonEval.eval(currBoard, wstats, bstats, who2Move);

        int opening = openingEval.eval(currBoard, wstats, bstats, who2Move);
        int endgame = endgameEval.eval(currBoard, wstats, bstats, who2Move);
        return taperedEval.eval(currBoard, common, opening, endgame);

    }

}
