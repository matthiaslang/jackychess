package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * Move Generator, which generates legal moves.
 */
public interface LegalMoveGenerator {

    MoveList generate(BoardRepresentation board, Color side);

}
