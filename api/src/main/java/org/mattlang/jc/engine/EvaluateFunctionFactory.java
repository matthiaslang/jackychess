package org.mattlang.jc.engine;

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

}
