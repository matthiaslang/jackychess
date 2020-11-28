package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;

public interface MoveCursor {

    void move(Board board);

    Move getMove();

    void undoMove(Board board);
}
