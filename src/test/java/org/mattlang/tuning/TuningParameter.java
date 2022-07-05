package org.mattlang.tuning;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

/**
 * Interface for all Tuning Parameters.
 */
public interface TuningParameter {

    /**
     * change the parameter by an offset of i. (Note it does only set the parameter, not update the value in any
     * evaluation function.
     */
    void change(int i);

    /**
     * Writes the tuning parameter value to the evaluation function.
     *
     * @param parameterizedEvaluation
     */
    void saveValue(ParameterizedEvaluation parameterizedEvaluation);
}
