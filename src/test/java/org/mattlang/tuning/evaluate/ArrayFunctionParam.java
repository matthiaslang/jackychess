package org.mattlang.tuning.evaluate;

import lombok.Getter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.IntIntervall;

@Getter
public class ArrayFunctionParam extends AbstractTuningParameter {

    private final int index;
    private final ArrayFunctionParameterGroup group;
    @Getter
    private final IntIntervall intervall;

    private int val;

    public ArrayFunctionParam(ArrayFunctionParameterGroup group, int index, int val, IntIntervall intervall) {
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
    public int getValue() {
        return val;
    }

    @Override
    public void setValue(int val) {
        this.val = val;
    }

    @Override
    public void saveValue(ParameterizedEvaluation parameterizedEvaluation) {
        group.setVal(parameterizedEvaluation, index, val);
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + step);
    }

    @Override
    public String getDescr() {
        return getGroup().getPropertyName() + ": " + index;
    }
}
