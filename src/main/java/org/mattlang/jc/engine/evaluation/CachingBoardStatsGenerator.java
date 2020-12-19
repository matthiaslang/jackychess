package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.movegenerator.BoardCache;

import static java.util.Objects.requireNonNull;

/**
 * Caches Board stats. Currently this doesnt give a performance improvement, mainly because evaulations
 * and legal moves are anyway cached, and these functions use boardstats internally...
 */
public class CachingBoardStatsGenerator implements BoardStatsGenerator {

    private BoardCache<BoardStats> cache = new BoardCache<>(this::generateStats);

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
