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

    enum GenMode {
        NORMAL,
        QUIESCENCE
    }

    default void generate(GenMode mode, GameContext gameContext,
            OrderCalculator orderCalculator,
            BoardRepresentation board,
            Color side,
            MoveList moveList) {
        throw new IllegalStateException("not implemented by this move generator!");
    }

    MoveList generate(BoardRepresentation board, Color side);

    /**
     * @param board current board
     * @param side  the side to move
     */
    void generate(BoardRepresentation board, Color side, MoveList moves);

}
