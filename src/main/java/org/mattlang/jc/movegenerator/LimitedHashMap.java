package org.mattlang.jc.movegenerator;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedHashMap<K,V> extends LinkedHashMap<K,V> {

    private static final int MAX_ENTRIES = 10000000;

    public LimitedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public LimitedHashMap() {
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }
}
