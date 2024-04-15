package org.mattlang.jc.engine.evaluation.parameval.functions;

import org.junit.Test;

public class ArrayFunctionTest {

    @Test
    public void testLinearInterpolation() {
        ArrayFunction f = new ArrayFunction(new int[]{-125,  -79, -152, -127, -124,  -62,  -52,  -27,  -27,   -6,   15,   20,   48,   49,   57,   63,   73,   62,   80,   67,   62,   41,   66,   27,   30,    0,   41,   30,    0});

        ArrayFunction interpolated = f.copy();
        interpolated.linearInterpolate();

        System.out.println(interpolated.convertDataToString());


        // use apache commons math 3 or 4: has interpoliation functions like spline interpolation

    }
}