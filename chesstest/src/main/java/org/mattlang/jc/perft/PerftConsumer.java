package org.mattlang.jc.perft;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;

@FunctionalInterface
public interface PerftConsumer {

    void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor);

}