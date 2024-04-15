package org.mattlang.jc.movegenerator;

/**
 * Modes during generation.
 */
public enum GenMode {
    /**
     * gen all moves.
     */
    NORMAL,
    /**
     * gen moves for quiescence search: only captures and promotions.
     */
    QUIESCENCE,

    /**
     * gen only quiet promotions. useful for experimental quiescence stage preparing the promotions first.
     */
    QUIESCENCE_PROMOTIONS,

    /**
     * gen only captures (without quiet promotions). experimental for quiescence stages to split between quiet
     * promotions and captures.
     */
    QUIESCENCE_CAPTURES
}
