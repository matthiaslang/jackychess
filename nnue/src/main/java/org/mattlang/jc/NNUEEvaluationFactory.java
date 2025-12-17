package org.mattlang.jc;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.EvaluateFunctionFactory;
import org.mattlang.jc.engine.nnue.NNUE;

public class NNUEEvaluationFactory implements EvaluateFunctionFactory {

    @Override
    public String getEvalImplName() {
        return "NNUE Evaluation";
    }

    @Override
    public EvaluateFunction createEvaluateFunction() {
        return new NNUE();
    }
}
