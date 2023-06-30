package org.mattlang.jc.engine.evaluation.parameval.functions;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;

/**
 * A function which is backed by an array to lookup the values.
 */
public final class ArrayFunction implements Function {

    private final int[] funVals;

    public ArrayFunction(int[] funVals) {
        this.funVals = funVals;
    }

    @Override
    public int calc(int x) {
        return funVals[x];
    }

    public static ArrayFunction parse(String data) {

        String[] splittedVals = data.split(",");
        int[] funVals = new int[splittedVals.length];
        int i = 0;
        for (String part : splittedVals) {
            funVals[i++] = Integer.parseInt(part.trim());
        }
        return new ArrayFunction(funVals);
    }

    public String convertDataToString() {
        return Arrays.stream(funVals)
                .mapToObj(v -> String.format("%1$4s", v))
                .collect(Collectors.joining(", "));
    }

    public ArrayFunction copy() {
        return ArrayFunction.parse(convertDataToString());
    }

    public int getSize() {
        return funVals.length;
    }

    public void setVal(int index, int val) {
        funVals[index] = val;
    }

    public static ArrayFunction combine(ArrayFunction funMG, ArrayFunction funEG) {
        int combinedSize = Math.max(funEG.getSize(), funMG.getSize());
        int[] vals = new int[combinedSize];
        for (int i = 0; i < combinedSize; i++) {
            int valMg = i < funMG.getSize() ? funMG.calc(i) : 0;
            int valEg = i < funEG.getSize() ? funEG.calc(i) : 0;
            vals[i] = MgEgScore.createMgEgScore(valMg, valEg);
        }
        ArrayFunction combined = new ArrayFunction(vals);
        return combined;
    }
}
