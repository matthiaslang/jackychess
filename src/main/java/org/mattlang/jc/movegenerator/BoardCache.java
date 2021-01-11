package org.mattlang.jc.movegenerator;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public interface BoardCache<T> extends StatisticsCollector {

    void setCreator(BoardCacheEntryCreator<T> creator);

    T getCache(BoardRepresentation board, Color side);

    T get(BoardRepresentation board, Color side);

    void put(BoardRepresentation board, Color side, T value);
}
