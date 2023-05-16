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

    /***
     * Returns the current set parameter value.
     * @return
     */
    int getValue();

    /**
     * sets the parameter value.
     */
    void setValue(int val);

    /**
     * returns the parameter interval.
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

    void addDependingFen(FenEntry fen);

    boolean hasDependingFens();

    String getDescr();

    int getDependingFenCount();

    /**
     * reset its value to a neutral value. (usually 0 or 1 depending if its additive or multiplicative).
     */
    void resetValue();
}
