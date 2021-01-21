package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;

/**
 * Tapered Evaluation function with special opening and endgame evaluate function.
 */
public class GamePhaseEvaluation implements EvaluateFunction {

    private OpeningEval openingEval = new OpeningEval();
    private EvaluateFunction endgameEval = new MaterialNegaMaxEvalOpt();

    private TaperedEval taperedEval = new TaperedEval();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        int opening = openingEval.eval(currBoard, who2Move);
        int endgame = endgameEval.eval(currBoard, who2Move);
        return taperedEval.eval(currBoard, opening, endgame);

    }

}
