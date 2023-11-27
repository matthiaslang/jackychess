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

    public static void updateCombined(ArrayFunction combinedMgEg, ArrayFunction funMG, ArrayFunction funEG) {
        for (int i = 0; i < combinedMgEg.getSize(); i++) {
            int valMg = i < funMG.getSize() ? funMG.calc(i) : 0;
            int valEg = i < funEG.getSize() ? funEG.calc(i) : 0;
            combinedMgEg.setVal(i, MgEgScore.createMgEgScore(valMg, valEg));
        }
    }

    /**
     * experimental: linear interpolates all values of the function.
     *
     * use apache commons math spline interpolation instead...!!!
     */
    public void linearInterpolate() {
        for (int i = 0; i < getSize(); i++) {
            if (i + 2 < getSize()) {
                int mid = calc(i + 1);

                int x1 = i;
                int x = i + 1;
                int x2 = i + 2;

                int y = interpolate(x1, x, x2);
                setVal(x, y);

            }
        }
    }

    private int interpolate(int x1, int x, int x2) {
        int y1 = calc(x1);
        int y2 = calc(x2);
        return y1 + (y2 - y1) / (x2 - x1) * (x - x1);
    }
}
