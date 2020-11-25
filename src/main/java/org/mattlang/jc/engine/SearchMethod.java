package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public interface SearchMethod {

    Move search(Board currBoard, int depth, Color color);
}
