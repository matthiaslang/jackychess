package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * Move Generator, which generates legal moves.
 */
public interface LegalMoveGenerator {

    MoveList generate(Board board, Color side);

}
