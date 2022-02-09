package org.mattlang.jc.engine.search;

public class SearchThreadContexts {

    private SearchThreadContext[] contexts = new SearchThreadContext[4];

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
}
