package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

import java.util.Objects;

public class CachingLegalMoveGenerator implements LegalMoveGenerator {

    private BoardCache<MoveList> legalMoveCache = new BoardCache<>(this::generateLegalMoves);

    private LegalMoveGenerator delegate;

    public CachingLegalMoveGenerator(LegalMoveGenerator delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        return legalMoveCache.getCache(board, side);
    }

    private MoveList generateLegalMoves(BoardRepresentation board, Color side) {
        return delegate.generate(board, side);
    }
}
