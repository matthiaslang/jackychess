package org.mattlang.jc.engine.evaluation;

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

    /**
     * Difference of score where we believe that we are the "winning" one. Used for weighting draws.
     */
    public static final int WINNING_WEIGHT = 5 * 100;

    /**
     * Weight for the situation where we believe that a draw would be very bad, because we believe we are the "winning"
     * one.  The value is smaller then a mate value so that we do not fall into mate distance pruning.
     */
    public static final int DRAW_IS_VERY_BAD = KING_WEIGHT / 2;
}
