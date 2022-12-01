package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.Intervall;
import org.mattlang.tuning.TuningParameter;

import lombok.Getter;

/**
 * Tuning parameter of a single value of a 64 field pattern.
 */
@Getter
public class PatternValueParam implements TuningParameter {

    private final int pos;
    private final PatternParameterGroup group;
    private final boolean mirrored;
    private final Intervall intervall;

    private int mirroredPos;
    private int val;

    public PatternValueParam(PatternParameterGroup group, int pos, int val, Intervall intervall) {
        this.group = group;
        this.pos = pos;
        this.val = val;
        mirrored = false;
        this.intervall=intervall;
    }

    public PatternValueParam(PatternParameterGroup group, int pos, int mirroredPos, int val, Intervall intervall) {
        this.group = group;
        this.pos = pos;
        this.mirroredPos = mirroredPos;
        this.mirrored = true;
        this.val = val;
        this.intervall=intervall;
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

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + step);
    }
}