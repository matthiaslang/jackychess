package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.engine.tt.IntCache;

/**
 * A thread safe eval cache.
 */
public class EvalCache {

    public final static IntCache instance = new IntCache(24);

    private EvalCache() {

    }

}
