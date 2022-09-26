package org.mattlang.jc.moves;

/**
 * Available stages useable in staged move generation.
 */
public enum Stage {

    HASH,
    KILLER1,
    KILLER2,
    COUNTER_MOVE,
    GOOD_CAPTURES,
    PROMOTIONS,
    QUIET,
    BAD_CAPTURES,
    ALL_CAPTURES,
    ALL
}
