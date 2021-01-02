package org.mattlang.jc.zobrist;

import java.util.HashMap;

public class ZobristHashMap<T> {

    public static final int CAPACITY = 50_000_000;

    private HashMap<Long, T> map = new HashMap<>(CAPACITY);

    public void add(long hash, T t) {
        map.put(hash, t);
    }

    public T find(long hash) {
        return map.get(hash);
    }

    public void remove(long hash) {
        map.remove(hash);
    }
}
