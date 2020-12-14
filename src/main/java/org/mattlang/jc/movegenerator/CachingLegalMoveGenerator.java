package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

public class CachingLegalMoveGenerator implements LegalMoveGenerator {

    private BoardCache<MoveList> legalMoveCache = new BoardCache<>(this::generateLegalMoves);

    private LegalMoveGenerator delegate = new LegalMoveGeneratorImpl2();

    @Override
    public MoveList generate(Board board, Color side) {
        return legalMoveCache.getCache(board, side);
    }

    private MoveList generateLegalMoves(Board board, Color side) {
        return delegate.generate(board, side);
    }
}
