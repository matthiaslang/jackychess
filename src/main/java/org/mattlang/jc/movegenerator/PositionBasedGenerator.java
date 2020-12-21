package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface PositionBasedGenerator<T> {

    T generate(BoardRepresentation board, Color side);
}
