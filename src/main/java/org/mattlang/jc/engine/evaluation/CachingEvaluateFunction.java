package org.mattlang.jc.engine.evaluation;

import java.util.HashMap;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;

/**
 * Caching Evaluation Function.
 */
public class CachingEvaluateFunction implements EvaluateFunction {

    private EvaluateFunction delegate;
    public static final int CAPACITY = 50_000_000;

    private HashMap<Board, Integer> whitemap = new HashMap<>(CAPACITY);
    private HashMap<Board, Integer> blackmap = new HashMap<>(CAPACITY);

    public CachingEvaluateFunction(EvaluateFunction delegate) {
        this.delegate = delegate;
    }

    @Override
    public int eval(Board currBoard, Color who2Move) {
        HashMap<Board, Integer> map = who2Move == Color.WHITE ? whitemap : blackmap;
        Integer cachedScore = map.get(currBoard);
        if (cachedScore != null) {
            return cachedScore;
        }

        int score = delegate.eval(currBoard, who2Move);
        map.put(currBoard, score);
        return score;

    }
}
