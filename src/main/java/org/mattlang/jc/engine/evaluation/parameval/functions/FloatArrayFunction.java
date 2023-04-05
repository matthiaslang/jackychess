package org.mattlang.jc.engine.evaluation.parameval.functions;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;

/**
 * A function which is backed by an array to lookup the values.
 */
public final class FloatArrayFunction {

    private final float[] funVals;

    public FloatArrayFunction(float[] funVals) {
        this.funVals = funVals;
    }

    public float calc(int x) {
        return funVals[x];
    }

    public static FloatArrayFunction parse(String data) {

        String[] splittedVals = data.split(",");
        float[] funVals = new float[splittedVals.length];
        int i = 0;
        for (String part : splittedVals) {
            funVals[i++] = Float.parseFloat(part.trim());
        }
        return new FloatArrayFunction(funVals);
    }

    public String convertDataToString() {
        ArrayList<String> nums = new ArrayList<>();
        for (float funVal : funVals) {
            nums.add(Float.toString(funVal));
        }
        return nums.stream().collect(joining(", "));
    }

    public FloatArrayFunction copy() {
        return FloatArrayFunction.parse(convertDataToString());
    }

    public int getSize() {
        return funVals.length;
    }

    public void setVal(int index, float val) {
        funVals[index] = val;
    }
}
