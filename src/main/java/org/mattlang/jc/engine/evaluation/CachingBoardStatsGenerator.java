package org.mattlang.jc.engine.evaluation;

import static java.util.Objects.requireNonNull;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.movegenerator.BoardCacheImpl;

/**
 * Caches Board stats. Currently this doesnt give a performance improvement, mainly because evaulations
 * and legal moves are anyway cached, and these functions use boardstats internally...
 */
public class CachingBoardStatsGenerator implements BoardStatsGenerator {

    private BoardCacheImpl<BoardStats> cache = new BoardCacheImpl<>(this::generateStats);

    private BoardStatsGenerator delegate;

    public CachingBoardStatsGenerator(BoardStatsGenerator delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public BoardStats gen(BoardRepresentation board, Color color) {
        return cache.getCache(board, color);
    }

    private BoardStats generateStats(BoardRepresentation board, Color color) {
        return delegate.gen(board, color);
    }
}
