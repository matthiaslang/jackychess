package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.COMMON_CAPTURABILITY_WEIGHT;
import static org.mattlang.jc.engine.evaluation.Weights.COMMON_MOBILITY_WEIGHT;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.Evaluations;

/**
 * Common evaluation:
 *
 * - simple material considerations
 * - mobility
 * - capturability
 *
 */
public class CommonEval {

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {

        int score = Evaluations.materialEval(currBoard, who2Move);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        score += COMMON_MOBILITY_WEIGHT * (wstats.mobility - bstats.mobility) * who2mov +
                COMMON_CAPTURABILITY_WEIGHT * (wstats.captures - bstats.captures) * who2mov;

        return score;

    }

}
