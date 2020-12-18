package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

public class CachingLegalMoveGenerator implements LegalMoveGenerator {

    private BoardCache<MoveList> legalMoveCache = new BoardCache<>(this::generateLegalMoves);

    private LegalMoveGenerator delegate = new LegalMoveGeneratorImpl2();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        return legalMoveCache.getCache(board, side);
    }

    private MoveList generateLegalMoves(BoardRepresentation board, Color side) {
        return delegate.generate(board, side);
    }
}
