package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;

public interface BoardCacheEntryCreator<T> {

    T createEntry(Board board, Color side);
}
