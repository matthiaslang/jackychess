package org.mattlang.jc.engine.tt;

import lombok.Getter;

/**
 * Holds the static initialized tt cache.
 * The cache is only created once on startup and then reused.
 */
public class Caching {

    @Getter
    private TTCache ttCache;

    public static final Caching CACHING = new Caching();

    private Caching() {
        ttCache = new TTCache();
    }

}
