package org.mattlang.jc.engine.evaluation.parameval.functions;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A function which is backed by an array to lookup the values.
 */
public class ArrayFunction implements Function {

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
                .mapToObj(v -> Integer.toString(v))
                .collect(Collectors.joining(", "));
    }
}
