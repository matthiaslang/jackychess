package org.mattlang.tuning.evaluate;

import java.io.File;

import org.mattlang.jc.engine.evaluation.annotation.configure.FieldAccessStack;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameterGroup;

/**
 * Group for a mg eg combined value which encapsulates the two individual tuning values for mg and eg.
 */
public class ChangeableMgEgScoreParameterGroup extends AbstractTuningParameterGroup {

    private final IntegerValueParam paramMg;

    private final IntegerValueParam paramEg;

    public ChangeableMgEgScoreParameterGroup(ParameterizedEvaluation parameterizedEvaluation,
            FieldAccessStack fieldAccessStack,
            String propMg, String propEg, IntIntervall intervall) {

        paramMg = new IntegerValueParam(propMg, parameterizedEvaluation,
                e -> MgEgScore.getMgScore((Integer) fieldAccessStack.get(e)),
                (e, val) -> fieldAccessStack.set(e, MgEgScore.setMg((Integer) fieldAccessStack.get(e), val)),
                intervall);
        getParameters().add(paramMg);

        paramEg = new IntegerValueParam(propEg, parameterizedEvaluation,
                e -> MgEgScore.getEgScore((Integer) fieldAccessStack.get(e)),
                (e, val) -> fieldAccessStack.set(e, MgEgScore.setEg((Integer) fieldAccessStack.get(e), val)),
                intervall);
        getParameters().add(paramEg);
    }

    public ChangeableMgEgScoreParameterGroup(ChangeableMgEgScoreParameterGroup src) {
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
