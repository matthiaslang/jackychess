package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.Intervall;
import org.mattlang.tuning.TuningParameter;

import lombok.Getter;

@Getter
public class ArrayFunctionParam implements TuningParameter {

    private final int index;
    private final ArrayFunctionParameterGroup group;
    @Getter
    private final Intervall intervall;

    private int val;

    public ArrayFunctionParam(ArrayFunctionParameterGroup group, int index, int val, Intervall intervall) {
        this.group = group;
        this.index = index;
        this.val = val;
        this.intervall = intervall;
    }

    @Override
    public void change(int offset) {
        val += offset;
    }

    @Override
    public void saveValue(ParameterizedEvaluation parameterizedEvaluation) {
        group.setVal(parameterizedEvaluation, index, val);
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + step);
    }
}
