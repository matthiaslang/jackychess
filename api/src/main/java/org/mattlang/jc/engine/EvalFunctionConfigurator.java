package org.mattlang.jc.engine;

import org.mattlang.jc.util.ServiceLoaderUtil;

public class EvalFunctionConfigurator {

    private static final EvaluateFunctionFactory factory =
            ServiceLoaderUtil.determineSingleService(EvaluateFunctionFactory.class);

    public static EvaluateFunction createConfiguredEvaluateFunction() {
        return factory.createEvaluateFunction();
    }

    public static String determineEvaluationFunctionName() {
        return factory.getEvalName();
    }
}
