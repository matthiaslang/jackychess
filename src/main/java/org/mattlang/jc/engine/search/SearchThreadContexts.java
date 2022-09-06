package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.MAX_THREADS;

public class SearchThreadContexts {

    private SearchThreadContext[] contexts = new SearchThreadContext[MAX_THREADS];

    public static SearchThreadContexts CONTEXTS = new SearchThreadContexts();

    private SearchThreadContexts() {
        reset();
    }

    public void reset() {
        for (int i = 0; i < contexts.length; i++) {
            contexts[i] = new SearchThreadContext();
        }
    }

    public SearchThreadContext getContext(int num) {
        return contexts[num];
    }

    // todo we should do that nicer... currently this is done on each uci go.. only for situations where the movelist param has been changed...
    public void resetMoveLists() {

        for (int i = 0; i < contexts.length; i++) {
            contexts[i].resetMoveLists();
        }
    }
}
