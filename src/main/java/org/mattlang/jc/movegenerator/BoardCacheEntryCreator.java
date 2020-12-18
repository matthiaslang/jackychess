package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface BoardCacheEntryCreator<T> {

    T createEntry(BoardRepresentation board, Color side);
}
