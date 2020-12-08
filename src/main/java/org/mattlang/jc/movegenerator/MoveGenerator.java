package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 */
public interface MoveGenerator {

    /**
     * @param board current board
     * @param side  the side to move
     */
    MoveList generate(Board board, Color side);

    /**
     * @param board current board
     * @param side  the side to move
     */
    MoveList generate(Board board, Color side, MoveList moves);

}
