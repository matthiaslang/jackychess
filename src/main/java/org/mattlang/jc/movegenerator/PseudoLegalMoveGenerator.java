package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;

/**
 * a "pseude legal" move generator, means, not really a legal move generator by definition, but the contrary.
 * Since this seems far faster than a real legal move generator, this will probably the future code
 * and we should remove the whole legal move generator interface or use it only for test purpose...
 */
public class PseudoLegalMoveGenerator implements MoveGenerator {

    BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();

    @Override
    public void generate(BoardRepresentation board, Color side, MoveList moveList) {
        generator.generate(board, side, moveList);
    }

    @Override
    public void generate(GenMode mode, OrderCalculator orderCalculator,
            BoardRepresentation board,
            Color side, MoveList moveList) {

        switch (mode) {
        case NORMAL:
            generator.generate(board, side, moveList);
            break;
        case QUIESCENCE:
            MoveGeneration.generateAttacks(board, side, moveList);
            MoveGeneration.genPawnQuietPromotions(board.getBoard(), moveList, side);
            break;
        }

    }

}
