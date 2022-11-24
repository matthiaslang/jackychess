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
     * Defines a  interval for the parameter. The tuning should not change the value out of its min/max bounds of this
     * intervall.
     *
     * @return
     */
    Intervall getIntervall();

    /**
     * Writes the tuning parameter value to the evaluation function.
     *
     * @param parameterizedEvaluation
     */
    void saveValue(ParameterizedEvaluation parameterizedEvaluation);

    /**
     * Is it possible to further tune this value by the given step?
     * Its not possible if the parameter would get out of its min/max bounds.
     *
     * @param step
     * @return
     */
    boolean isChangePossible(int step);
}
