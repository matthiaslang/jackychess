package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.engine.tt.IntIntCache;

/**
 * A thread safe eval cache.
 */
public class EvalCache {

    public final static IntIntCache instance = new IntIntCache(25);
    public final static IntIntCache pawnCache = new IntIntCache(23);

    private EvalCache() {

    }

}
