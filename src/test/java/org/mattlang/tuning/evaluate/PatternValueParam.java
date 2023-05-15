package org.mattlang.tuning.evaluate;

import lombok.Getter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.IntIntervall;

/**
 * Tuning parameter of a single value of a 64 field pattern.
 */
@Getter
public class PatternValueParam extends AbstractTuningParameter {

    private final int pos;
    private final PatternParameterGroup group;
    private final boolean mirrored;
    private final IntIntervall intervall;

    private int mirroredPos;
    private int val;

    public PatternValueParam(PatternParameterGroup group, int pos, int val, IntIntervall intervall) {
        this.group = group;
        this.pos = pos;
        this.val = val;
        mirrored = false;
        this.intervall = intervall;
    }

    public PatternValueParam(PatternParameterGroup group, int pos, int mirroredPos, int val, IntIntervall intervall) {
        this.group = group;
        this.pos = pos;
        this.mirroredPos = mirroredPos;
        this.mirrored = true;
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
        group.setVal(parameterizedEvaluation, pos, val);
        if (mirrored) {
            group.setVal(parameterizedEvaluation, mirroredPos, val);
        }
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + step);
    }

    @Override
    public String getDescr() {
        return getGroup().getTableCsvName() + ": " + pos;
    }
}
