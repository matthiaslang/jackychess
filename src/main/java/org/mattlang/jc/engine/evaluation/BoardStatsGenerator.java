package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

/**
 * Generate static Board Statistics like mobility, captures...
 */
public interface BoardStatsGenerator {

    BoardStats gen(BoardRepresentation board, Color color);
}
