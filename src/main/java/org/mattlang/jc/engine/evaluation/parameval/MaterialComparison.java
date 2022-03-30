package org.mattlang.jc.engine.evaluation.parameval;

/**
 * Comparisons of Material of a Material Description.
 */
public enum MaterialComparison {

    /* complete unspecified. this makes only sense for the weaker side: means it is unspecified, but anyway lower/weaker than the stronger side. */
    UNSPECIFIED,

    /** Equal material*/
    EQUAL,

    /** Compares to have more material*/
    MORE,

    /** More ore equal material. */
    MORE_OR_EQUAL
}
