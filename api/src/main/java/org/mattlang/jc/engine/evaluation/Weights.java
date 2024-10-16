package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.Constants.MAX_PLY;

/**
 * Defines Weights used in the evaluation functions.
 */
public class Weights {

    /* King weight, aka Matt Weight. */
    public static final int KING_WEIGHT = 32000;

    /**
     * Patt weight.
     */
    public static final int PATT_WEIGHT = 0;

    public static final int REPETITION_WEIGHT = 0;

    public static final int VALUE_TB_WIN_IN_MAX_PLY = KING_WEIGHT - 2 * MAX_PLY;
    public static final int VALUE_TB_LOSS_IN_MAX_PLY = -VALUE_TB_WIN_IN_MAX_PLY;
}
