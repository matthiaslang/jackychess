package org.mattlang.jc.perft;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.moves.MoveBoardIterator;

public interface PerftIteratorSupplier {

    MoveBoardIterator supplyIterator(int depth, BoardRepresentation board, Color color);
}
