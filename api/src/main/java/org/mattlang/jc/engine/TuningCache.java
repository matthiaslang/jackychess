package org.mattlang.jc.engine;

import java.util.Arrays;

public class TuningCache {

    private final int[] cache = new int[EvalComponentName.values().length];

    public TuningCache() {
        Arrays.fill(cache, Integer.MIN_VALUE);
    }

    public int getEvalSum() {
        int sum = 0;
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] != Integer.MIN_VALUE) {
                sum += cache[i];
            }
        }
        return sum;
    }

    public enum EvalComponentName {
        MOB,
        PAWN,
        COMPLEXITY,
        ADJUSTMENTS,
        THREATS,
        PST,
        KING,
        MAT,
        SPACE
    }

    public int get(EvalComponentName name) {
        return cache[name.ordinal()];
    }

    public void put(EvalComponentName name, int newVal) {
        cache[name.ordinal()] = newVal;
    }

    public void putAll(TuningCache tuningCache) {
        for (int i = 0; i < cache.length; i++) {
            if (tuningCache.cache[i] == Integer.MIN_VALUE) {
                // illegal state: not every value has been set during eval calculation
                throw new IllegalArgumentException();
            }
            cache[i] = tuningCache.cache[i];
        }
    }

    public void updateFromFen(TuningCache tuningCache) {
        System.arraycopy(tuningCache.cache, 0, cache, 0, cache.length);
    }

    public void clear(EvalComponentName name) {
        put(name, Integer.MIN_VALUE);

    }
}
