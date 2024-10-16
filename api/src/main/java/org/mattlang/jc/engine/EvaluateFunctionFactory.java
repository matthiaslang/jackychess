package org.mattlang.jc.engine;

import org.mattlang.jc.util.ServiceLoaderUtil;

/**
 * Factory to create Evaluate Functions.
 * Used to configure Factories and implementations of Evaluate Functions via ServiceLoader.
 */
public interface EvaluateFunctionFactory {

    String getEvalName();

    /**
     * Creates a new instance of an evaluation factory implementation.
     *
     * @return
     */
    EvaluateFunction createEvaluateFunction();

    public static EvaluateFunction createConfiguredEvaluateFunction() {
        EvaluateFunctionFactory factory = ServiceLoaderUtil.determineSingleService(EvaluateFunctionFactory.class);
        return factory.createEvaluateFunction();
    }

    public static String determineEvaluationFunctionName() {
        EvaluateFunctionFactory factory = ServiceLoaderUtil.determineSingleService(EvaluateFunctionFactory.class);
        return factory.getEvalName();
    }

}
