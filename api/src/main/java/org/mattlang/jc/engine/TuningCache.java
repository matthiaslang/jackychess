package org.mattlang.jc.engine;

import java.util.EnumMap;

public class TuningCache {

    public int getEvalSum() {
        int sum = 0;
        for (Integer value : tuningCache.values()) {
            sum += value;
        }
        return sum;
    }

    public static enum EvalComponentName {
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

    private EnumMap<EvalComponentName, Integer> tuningCache = new EnumMap<>(EvalComponentName.class);

    public Integer get(EvalComponentName name) {
        return tuningCache.get(name);
    }

    public void put(EvalComponentName name, int diff) {
        tuningCache.put(name, diff);
    }

    public void putAll(TuningCache tuningCache) {
        if (tuningCache.tuningCache.size() != EvalComponentName.values().length) {
            throw new IllegalArgumentException(
                    "tuningCache must have " + EvalComponentName.values().length + " elements");
        }
        for (EvalComponentName evalComponentName : EvalComponentName.values()) {
            if (tuningCache.get(evalComponentName) == null) {
                throw new IllegalArgumentException();
            }
        }
        this.tuningCache.clear();
        this.tuningCache.putAll(tuningCache.tuningCache);
    }

    public void updateFromFen(TuningCache tuningCache) {
        this.tuningCache.clear();
        this.tuningCache.putAll(tuningCache.tuningCache);
    }

    public void clear(EvalComponentName name) {
        this.tuningCache.put(name, null);
    }
}
