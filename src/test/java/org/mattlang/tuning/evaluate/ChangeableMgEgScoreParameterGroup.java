package org.mattlang.tuning.evaluate;

import java.io.File;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ChangeableMgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameterGroup;

/**
 * Group for a mg eg combined value which encapsulates the two individual tuning values for mg and eg.
 */
public class ChangeableMgEgScoreParameterGroup extends AbstractTuningParameterGroup {

    private final Function<ParameterizedEvaluation, ChangeableMgEgScore> getter;

    private final IntegerValueParam paramMg;

    private final IntegerValueParam paramEg;

    public ChangeableMgEgScoreParameterGroup(ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedEvaluation, ChangeableMgEgScore> getter, IntIntervall intervall) {
        this.getter = getter;

        ChangeableMgEgScore mgEgScore = getter.apply(parameterizedEvaluation);

        paramMg = new IntegerValueParam(mgEgScore.getPropNameMg(), parameterizedEvaluation,
                e -> getter.apply(e).getMgScore(),
                (e, val) -> getter.apply(e).setMg(val),
                intervall);
        getParameters().add(paramMg);

        paramEg = new IntegerValueParam(mgEgScore.getPropNameEg(), parameterizedEvaluation,
                e -> getter.apply(e).getEgScore(),
                (e, val) -> getter.apply(e).setEg(val),
                intervall);
        getParameters().add(paramEg);
    }

    public ChangeableMgEgScoreParameterGroup(ChangeableMgEgScoreParameterGroup src) {
        this.getter = src.getter;
        this.paramMg = (IntegerValueParam) src.paramMg.copy();
        this.paramEg = (IntegerValueParam) src.paramEg.copy();
    }

    @Override
    public String getParamDef() {
        return paramMg.getParamDef() + "\n" + paramEg.getParamDef();
    }

    @Override
    public void writeParamDef(File outputDir) {
        paramMg.writeParamDef(outputDir);
        paramEg.writeParamDef(outputDir);
    }

    @Override
    public TuningParameterGroup copy() {
        return new ChangeableMgEgScoreParameterGroup(this);
    }

}
