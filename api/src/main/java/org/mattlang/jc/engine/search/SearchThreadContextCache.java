package org.mattlang.jc.engine.search;

import java.util.HashMap;
import java.util.Map;

/**
 * contains caches for one search thread.
 * Users must set and reuse the cache on their own.
 */
public class SearchThreadContextCache {

    private Map<String, ContextCache> cache = new HashMap<>();

    public ContextCache getCacheObject(String key) {
        return cache.get(key);
    }

    public void setCacheObject(String key, ContextCache value) {
        cache.put(key, value);
    }

    public void reset() {
        cache.values().forEach(ContextCache::reset);
    }
}
