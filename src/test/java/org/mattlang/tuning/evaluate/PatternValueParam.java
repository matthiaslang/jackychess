package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameter;

import lombok.Getter;

/**
 * Tuning parameter of a single value of a 64 field pattern.
 */
@Getter
public class PatternValueParam extends AbstractTuningParameter {

    private final PatternParameterGroup group;
    private final int pos;
    private final boolean mirrored;
    private final IntIntervall intervall;
    private final ScoreType scoreType;

    private int mirroredPos;
    private int val;

    private PatternValueParam(PatternParameterGroup group, PatternValueParam orig) {
        this.group = group;
        this.scoreType = orig.getScoreType();
        this.pos = orig.pos;
        this.mirrored = orig.mirrored;
        this.intervall = orig.intervall;
        this.mirroredPos = orig.mirroredPos;
        this.val = orig.val;
    }

    public PatternValueParam(PatternParameterGroup group, ScoreType scoreType, int pos, int val,
            IntIntervall intervall) {
        this.group = group;
        this.scoreType = scoreType;
        this.pos = pos;
        this.val = val;
        mirrored = false;
        this.intervall = intervall;
    }

    public PatternValueParam(PatternParameterGroup group, ScoreType scoreType, int pos, int mirroredPos, int val,
            IntIntervall intervall) {
        this.group = group;
        this.scoreType = scoreType;
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
        group.setVal(parameterizedEvaluation, scoreType, pos, val);
        if (mirrored) {
            group.setVal(parameterizedEvaluation, scoreType, mirroredPos, val);
        }
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(val + step);
    }

    @Override
    public String getDescr() {
        return getGroup().getParamName() + " " + scoreType.name() + ": " + pos;
    }

    @Override
    public void resetValue() {
        val = 0;
    }

    public TuningParameter copyParam(PatternParameterGroup group) {
        return new PatternValueParam(group, this);
    }
}
