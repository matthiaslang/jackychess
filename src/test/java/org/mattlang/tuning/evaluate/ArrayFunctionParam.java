package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.TuningParameter;

import lombok.Getter;

@Getter
public class ArrayFunctionParam implements TuningParameter {

    private final int index;
    private final ArrayFunctionParameterGroup group;

    private int val;

    public ArrayFunctionParam(ArrayFunctionParameterGroup group, int index, int val) {
        this.group = group;
        this.index = index;
        this.val = val;

    }

    @Override
    public void change(int offset) {
        val += offset;
    }

    @Override
    public void saveValue(ParameterizedEvaluation parameterizedEvaluation) {
        group.setVal(parameterizedEvaluation, index, val);
    }
}
