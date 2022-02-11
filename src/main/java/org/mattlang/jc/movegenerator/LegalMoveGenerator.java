package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.uci.GameContext;

/**
 * Move Generator, which generates legal moves.
 */
public interface LegalMoveGenerator extends PositionBasedGenerator<MoveList> {

    void generate(GameContext gameContext,
            OrderCalculator orderCalculator,
            BoardRepresentation board,
            Color side,
            MoveList moveList);

    void generate(BoardRepresentation board, Color who2Move, MoveList moveList);
}
