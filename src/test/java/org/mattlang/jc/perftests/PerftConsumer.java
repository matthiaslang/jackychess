package org.mattlang.jc.perftests;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

@FunctionalInterface
public interface PerftConsumer {

    void accept(BoardRepresentation board, Color color, int depth);
}