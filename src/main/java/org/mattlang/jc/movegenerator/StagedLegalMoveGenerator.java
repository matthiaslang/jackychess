package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.moves.StagedMoveListImpl;
import org.mattlang.jc.uci.GameContext;

/**
 * a "pseude legal" move generator, means, not really a legal move generator by definition, but the contrary.
 * Since this seems far faster than a real legal move generator, this will probably the future code
 * and we should remove the whole legal move generator interface or use it only for test purpose...
 *
 * This is a staged move generator version which creates all moves in different lazily in different stages.
 */
public class StagedLegalMoveGenerator implements LegalMoveGenerator {

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        throw new IllegalStateException("not supported in staged move generation!");
    }

    @Override
    public void generate(GameContext gameContext, OrderCalculator orderCalculator, BoardRepresentation board,
            Color side, MoveList moveList) {
        if (!(moveList instanceof StagedMoveListImpl)) {
            throw new IllegalStateException("needs StagedMoveListImpl!");
        }
        ((StagedMoveListImpl) moveList).init(gameContext, orderCalculator, board, side);
    }

    @Override
    public void generate(BoardRepresentation board, Color who2Move, MoveList moveList) {
        throw new IllegalStateException("not supported!");
    }
}
