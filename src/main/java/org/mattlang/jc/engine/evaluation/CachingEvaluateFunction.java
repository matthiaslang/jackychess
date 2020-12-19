package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.movegenerator.BoardCache;

/**
 * Caching Evaluation Function.
 */
public class CachingEvaluateFunction implements EvaluateFunction {

    private EvaluateFunction delegate;

    private BoardCache<Integer> cache = new BoardCache<>(this::evaluate);

    private Integer evaluate(BoardRepresentation board, Color color) {
        return delegate.eval(board, color);
    }

    public CachingEvaluateFunction(EvaluateFunction delegate) {
        this.delegate = delegate;
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return cache.getCache(currBoard, who2Move);
    }
}
