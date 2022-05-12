package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface EvaluateFunction {

    int eval(BoardRepresentation currBoard, Color who2Move);

}
