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
    QUIESCENCE
}
