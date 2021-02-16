package org.mattlang.jc.movegenerator;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

public class CachingLegalMoveGenerator implements LegalMoveGenerator, StatisticsCollector {

    private BoardCache<MoveList> legalMoveCache = new BoardCacheImpl<>(this::generateLegalMoves);

    private LegalMoveGenerator delegate;

    public CachingLegalMoveGenerator(LegalMoveGenerator delegate) {
        this.delegate = requireNonNull(delegate);
    }

    public CachingLegalMoveGenerator(BoardCache<MoveList> legalMoveCache, LegalMoveGenerator delegate) {
        this.delegate = requireNonNull(delegate);
        this.legalMoveCache = requireNonNull(legalMoveCache);
        this.legalMoveCache.setCreator(this::generateLegalMoves);
    }

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        return legalMoveCache.getCache(board, side);
    }

    private MoveList generateLegalMoves(BoardRepresentation board, Color side) {
        return delegate.generate(board, side);
    }

    @Override
    public void resetStatistics() {
        legalMoveCache.resetStatistics();
    }

    @Override
    public void collectStatistics(Map stats) {
        Map nested = new LinkedHashMap();
        stats.put("caching legal moves", nested);
        legalMoveCache.collectStatistics(nested);
    }

    @Override
    public MoveList generateNonQuietMoves(BoardRepresentation board, Color side) {
        throw new IllegalArgumentException("not implemented!");
    }
}
