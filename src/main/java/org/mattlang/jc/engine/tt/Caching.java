package org.mattlang.jc.engine.tt;

import org.mattlang.jc.Factory;

import lombok.Getter;

/**
 * Holds the static initialized tt cache.
 * The cache is only created once on startup and then reused.
 */
public class Caching {

    @Getter
    private TTCacheInterface ttCache;

    public static final Caching CACHING = new Caching();

    private Caching() {
        ttCache = Factory.getDefaults().getConfig().cacheImpls.getValue().createSupplier().get();
    }

}
