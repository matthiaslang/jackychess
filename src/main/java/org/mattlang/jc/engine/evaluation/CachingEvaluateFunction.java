package org.mattlang.jc.engine.evaluation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.movegenerator.BoardCache;
import org.mattlang.jc.movegenerator.BoardCacheImpl;

/**
 * Caching Evaluation Function.
 */
public class CachingEvaluateFunction implements EvaluateFunction, StatisticsCollector {

    private EvaluateFunction delegate;

    private BoardCache<Integer> cache = new BoardCacheImpl<>(this::evaluate);

    private Integer evaluate(BoardRepresentation board, Color color) {
        return delegate.eval(board, color);
    }

    public CachingEvaluateFunction(EvaluateFunction delegate) {
        this.delegate = delegate;
    }

    public CachingEvaluateFunction(BoardCache<Integer> cache, EvaluateFunction delegate) {
        this.delegate = delegate;
        this.cache = cache;
        this.cache.setCreator(this::evaluate);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return cache.getCache(currBoard, who2Move);
    }

    @Override
    public void resetStatistics() {
        cache.resetStatistics();
    }

    @Override
    public void collectStatistics(Map stats) {
        Map nested = new LinkedHashMap();
        stats.put("caching evaluation", nested);
        cache.collectStatistics(nested);
    }
}
