package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.PawnCache;

public interface EvaluateFunction {

    int eval(BoardRepresentation currBoard, Color who2Move);

    void setPawnCache(PawnCache pawnCache);
}
