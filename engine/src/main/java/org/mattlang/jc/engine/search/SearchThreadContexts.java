package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.MAX_THREADS;

import org.mattlang.jc.engine.evaluation.parameval.EvalCache;

public class SearchThreadContexts {

    private SearchThreadContext[] contexts = new SearchThreadContext[MAX_THREADS];

    public static SearchThreadContexts CONTEXTS = new SearchThreadContexts();

    private SearchThreadContexts() {
        for (int i = 0; i < contexts.length; i++) {
            contexts[i] = new SearchThreadContext();
        }
    }

    /**
     * Reset all internal data structures.
     */
    public void reset() {
        EvalCache.instance.reset();
        for (int i = 0; i < contexts.length; i++) {
            contexts[i].reset();
        }
    }

    public SearchThreadContext getContext(int num) {
        return contexts[num];
    }

}
