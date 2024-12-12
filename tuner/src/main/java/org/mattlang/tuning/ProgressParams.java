package org.mattlang.tuning;

/**
 * Holds current parameter about the progress of the tuning mainly for output.
 */
public class ProgressParams {
    /** current best e. */
    double bestE;

    int round = 0;
    int numParamAdjusted = 0;

    int paramIterationRound = 0;
    boolean improved = true;

}
