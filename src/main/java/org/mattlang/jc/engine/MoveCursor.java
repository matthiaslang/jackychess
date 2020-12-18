package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

public interface MoveCursor {

    void move(BoardRepresentation board);

    Move getMove();

    void undoMove(BoardRepresentation board);
}
