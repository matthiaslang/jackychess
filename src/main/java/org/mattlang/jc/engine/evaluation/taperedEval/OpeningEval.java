package org.mattlang.jc.engine.evaluation.taperedEval;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.evaltables.Opening;

/**
 * Evaluation for Opening Phase.
 * <p>
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class OpeningEval {

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {
        // apply opening patterns:
        int score = Opening.openingPatterns(currBoard, who2Move);
        return score;

    }

}
