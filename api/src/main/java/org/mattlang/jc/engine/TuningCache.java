package org.mattlang.jc.engine;

import java.util.EnumMap;

public class TuningCache {



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

    private EvalComponentName fromStr(String string) {
        return EvalComponentName.valueOf(string.toUpperCase());
    }

    public Integer get(String prefix) {
        return tuningCache.get(fromStr(prefix));
    }

    public void put(String prefix, int diff) {
        tuningCache.put(fromStr(prefix), diff);
    }

    public void putAll(TuningCache tuningCache) {
        if (tuningCache.tuningCache.size() != EvalComponentName.values().length) {
            throw new IllegalArgumentException(
                    "tuningCache must have " + EvalComponentName.values().length + " elements");
        }
        this.tuningCache.clear();
        this.tuningCache.putAll(tuningCache.tuningCache);
    }

    public void updateFromFen(TuningCache tuningCache) {
        this.tuningCache.clear();
        this.tuningCache.putAll(tuningCache.tuningCache);
    }

    public void clear(String evalCompName) {
        this.tuningCache.put(fromStr(evalCompName), null);
    }
}
