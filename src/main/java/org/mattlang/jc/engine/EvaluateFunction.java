package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.parameval.PawnCache;

public interface EvaluateFunction {

    int eval(BoardRepresentation currBoard, Color who2Move);

    /**
     * Calculates the Delta of PST Values when moving the figure. It calculates only the delta for quiet moves.
     * Captures are not considered.
     *
     * Positive values mean a benefit, negative values mean a disadvantage for the players static eval.
     *
     * @param color
     * @param m
     */
    int calcPstDelta(Color color, Move m);

    void setPawnCache(PawnCache pawnCache);
}
