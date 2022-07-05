package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.TuningParameter;

import lombok.Getter;

@Getter
public class PstValueParam implements TuningParameter {

    private final int pos;
    private final PstPatternParameterGroup group;
    private final boolean mirrored;
    private int mirroredPos;
    private int val;

    public PstValueParam(PstPatternParameterGroup group, int pos, int val) {
        this.group = group;
        this.pos = pos;
        this.val = val;
        mirrored = false;
    }

    public PstValueParam(PstPatternParameterGroup group, int pos, int mirroredPos, int val) {
        this.group = group;
        this.pos = pos;
        this.mirroredPos = mirroredPos;
        this.mirrored = true;
        this.val = val;
    }

    @Override
    public void change(int offset) {
        val += offset;
    }

    @Override
    public void saveValue(ParameterizedEvaluation parameterizedEvaluation) {
        group.setVal(parameterizedEvaluation, pos, val);
        if (mirrored) {
            group.setVal(parameterizedEvaluation, mirroredPos, val);
        }
    }
}
