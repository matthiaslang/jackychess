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

    public static final int REPETITION_WEIGHT = 9000;

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

    public static final int  DOUBLED_PAWN_PENALTY= -10;
    public static final int  ISOLATED_PAWN_PENALTY = -20;
    public static final int  BACKWARDS_PAWN_PENALTY =-8;
    public static final int  PASSED_PAWN_BONUS = 20;

    /**
     * Penalty if we have too less material to win in endgame:
     */
    public static final int TOO_LESS_MATERIAL_PENALTY = -400;

    public static final int NOT_ENOUGH_MORE_MATERIAL_THAN_OPPONENT = -400;

    /**
     * King Safety penalties:
     */

    public static final int OPEN_FILE_NEAR_KING_PENALTY = -10;

    public static final int KING_SAFETY_NO_PAWN_ON_THIS_FILE = -25;
    public static final int KING_SAFETY_PAWN_MOVED_ONE_SQUARE = -10;
    public static final int KING_SAFETY_PAWN_MOVED_MORE_THAN_A_SQUARE = -20;
    public static final int KING_SAFETY_ENEMY_ON_THIRD_RANK = -10;
    public static final int KING_SAFETY_ENEMY_ON_FOURTH_RANK = -5;



    /**
     * Maximum Material of all figures.
     */
    public static final int MAX_MAT = KING_WEIGHT + QUEEN_WEIGHT + ROOK_WEIGHT*2 + BISHOP_WEIGHT*2 + KNIGHT_WEIGHT*2 + PAWN_WEIGHT*8;

    /**
     * "Real" material weight is often without king, because king has a pseudo high value and the king is anyway alwaysin the game, otherwise we have lost..
     */
    public static final int MAX_REAL_MAT = MAX_MAT - KING_WEIGHT;
}
