package org.mattlang.jc;

/**
 * Different evaluation parameter sets to play and test around.
 */
public enum EvalParameterSet {

    /**
     * only Material Evaluation.  Used as Reference for the "simplest" evaluation.
     */
    DEFAULT,

    /**
     * Current best evaluation with several evaluation terms.
     */
    CURRENT,

    /**
     * Experimental evaluation set to try out different parameters and compare with other sets.
     */
    EXPERIMENTAL,

    /**
     * Parameterset of the version 0.12.0.
     * */
    V_0_12_0,

    /**
     * Parameterset of the version 0.13.0.
     * */
    V_0_13_0,

    /**
     * Parameterset of the version 0.14.3.
     * */
    V_0_14_3,

    /** experiments with tuned parameter configs. */
    TUNED01,

    
    TUNED02,

    TUNED03
}
