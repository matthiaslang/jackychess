package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface StalemateChecker {

    /**
     * Returns either 0 or the stalemate evaluation value.
     * @param currBoard
     * @param who2Move
     * @return
     */
     int eval(BoardRepresentation currBoard, Color who2Move);
}
