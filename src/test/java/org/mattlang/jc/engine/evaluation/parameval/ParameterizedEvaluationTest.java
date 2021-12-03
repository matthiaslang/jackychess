package org.mattlang.jc.engine.evaluation.parameval;

import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;

public class ParameterizedEvaluationTest {

    @Test
    public void configParseTest(){

        // try load/parse the default config:
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.DEFAULT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL);
        pe = new ParameterizedEvaluation();
    }
}