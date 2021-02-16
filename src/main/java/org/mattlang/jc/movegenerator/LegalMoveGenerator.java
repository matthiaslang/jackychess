package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * Move Generator, which generates legal moves.
 */
public interface LegalMoveGenerator extends PositionBasedGenerator<MoveList>{

    /**
     * Generate "non quiete" moves. Moves which are of interest in quiescence.
     * These are
     * 1. captures
     * 2. promotions
     * 3. moves which check the opponent
     *
     * @param board
     * @param side
     * @return
     */
    MoveList generateNonQuietMoves(BoardRepresentation board, Color side);
}
