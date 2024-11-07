package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.EvaluateFunctionFactory;

public class ParameterizedEvaluationFactory implements EvaluateFunctionFactory {

    @Override
    public String getEvalImplName() {
        return "Parameterized HCE Evaluation";
    }

    @Override
    public EvaluateFunction createEvaluateFunction() {
        return new ParameterizedEvaluation();
    }
}
