package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;

public interface EvaluateFunction {

    int eval(Board currBoard, Color who2Move);
}
