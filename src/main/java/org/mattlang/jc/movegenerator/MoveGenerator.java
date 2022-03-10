package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.uci.GameContext;

/**
 * interface for move generators.
 */
public interface MoveGenerator {

    /**
     * Modes during generation.
     */
    enum GenMode {
        /**
         * gen all moves.
         */
        NORMAL,
        /**
         * gen moves for quiescence search: only captures and promotions.
         */
        QUIESCENCE
    }

    /**
     * Generate moves from a given position for a given side.
     *
     * @param mode            either all or only for quiescence search
     * @param gameContext     the game context, holding e.g. hash moves
     * @param orderCalculator order information which might be used already for ordering the moves
     * @param board           the position
     * @param side            the side to generate the moves for
     * @param moveList        the resulting move list. given as argument. The Caller should reuse the movelist as much
     *                        as possible to prevent garbage collection
     */
    default void generate(GenMode mode, GameContext gameContext,
            OrderCalculator orderCalculator,
            BoardRepresentation board,
            Color side,
            MoveList moveList) {
        throw new IllegalStateException("not implemented by this move generator!");
    }

    /**
     * Generates moves.
     *
     * @param board current board
     * @param side  the side to move
     * @param moves the resulting move list. given as argument. The Caller should reuse the movelist as much
     *              as possible to prevent garbage collection
     */
    void generate(BoardRepresentation board, Color side, MoveList moves);

}
