package org.mattlang.tuning.evaluate;

import lombok.Getter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.FloatIntervall;

@Getter
public class FloatArrayFunctionParam extends AbstractTuningParameter {

    private final int index;
    private final FloatArrayFunctionParameterGroup group;
    @Getter
    private final FloatIntervall intervall;

    private float val;

    public FloatArrayFunctionParam(FloatArrayFunctionParameterGroup group, int index, float val, FloatIntervall intervall) {
        this.group = group;
        this.index = index;
        this.val = val;
        this.intervall = intervall;
    }

    @Override
    public void change(int offset) {
        val += FloatValueParam.calcChange(offset);
    }

    @Override
    public int getValue() {
        return (int) (val * 10);
    }

    @Override
    public void setValue(int val) {
        this.val = FloatValueParam.calcChange(val);
    }

    @Override
    public void saveValue(ParameterizedEvaluation parameterizedEvaluation) {
        group.setVal(parameterizedEvaluation, index, val);
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + FloatValueParam.calcChange(step));
    }

    @Override
    public String getDescr() {
        return getGroup().getPropertyName() + ": " + index;
    }

    @Override
    public void resetValue() {
        val = 1;
    }
}
