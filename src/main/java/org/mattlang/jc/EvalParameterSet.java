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

    /** experiments with tuned parameter configs. */
    TUNED01,

    
    TUNED02,

    TUNED03
}
