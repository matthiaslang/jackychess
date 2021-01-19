package org.mattlang.jc.engine.evaluation;

/**
 * Defines Weights used in the evaluation functions.
 */
public class Weights {

    /* King wheight, aka Matt Weight. */
    public static final int KING_WEIGHT = 32000;

    /**
     * Patt weight.
     */
    public static final int PATT_WEIGHT = 10000;

    public static final int QUEEN_WEIGHT = 900;
    public static final int ROOK_WEIGHT = 500;
    public static final int BISHOP_WEIGHT = 330;
    public static final int KNIGHT_WEIGHT = 320;
    public static final int PAWN_WEIGHT = 100;

    public static final int TWO_BISHOP_BONUS = 50;

    public static final int TWO_ROOKS_PENALTY = -50;

    public static final int NO_PAWNS_PENALTY = -500;

    public static final int COMMON_MOBILITY_WEIGHT = 10;
    public static final int COMMON_CAPTURABILITY_WEIGHT = 20;

}
