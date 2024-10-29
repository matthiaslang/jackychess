package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * a "pseude legal" move generator.
 */
public class PseudoLegalMoveGenerator {

    /**
     * Generates moves.
     *
     * @param board    current board
     * @param side     the side to move
     * @param moveList the resulting move list. given as argument. The Caller should reuse the movelist as much
     *                 as possible to prevent garbage collection
     */
    public void generate(BoardRepresentation board, Color side, MoveList moveList) {
        MoveGeneration.generateAttacks(board, side, moveList);
        MoveGeneration.generateQuiets(board, side, moveList);
    }

    /**
     * Generate moves from a given position for a given side.
     *
     * @param mode     either all or only for quiescence search
     * @param board    the position
     * @param side     the side to generate the moves for
     * @param moveList the resulting move list. given as argument. The Caller should reuse the movelist as much
     *                 as possible to prevent garbage collection
     */
    public void generate(GenMode mode,
            BoardRepresentation board,
            Color side, MoveList moveList) {

        switch (mode) {
        case NORMAL:
            MoveGeneration.generateAttacks(board, side, moveList);
            MoveGeneration.generateQuiets(board, side, moveList);
            break;
        case QUIESCENCE:
            MoveGeneration.generateAttacks(board, side, moveList);
            MoveGeneration.genPawnQuietPromotions(board.getBoard(), moveList, side);
            break;
        case QUIESCENCE_PROMOTIONS:
            MoveGeneration.genPawnQuietPromotions(board.getBoard(), moveList, side);
            break;
        case QUIESCENCE_CAPTURES:
            MoveGeneration.generateAttacks(board, side, moveList);
            break;
        }

    }

}
