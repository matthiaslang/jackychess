package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.uci.GameContext;

/**
 * a "pseude legal" move generator, means, not really a legal move generator by definition, but the contrary.
 * Since this seems far faster than a real legal move generator, this will probably the future code
 * and we should remove the whole legal move generator interface or use it only for test purpose...
 */
public class PseudoLegalMoveGenerator implements MoveGenerator {

    BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
    BBMoveGeneratorImpl2 generator2 = new BBMoveGeneratorImpl2();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        return moves;
    }

    @Override
    public void generate(BoardRepresentation board, Color side, MoveList moveList) {
        generator.generate(board, side, moveList);
    }

    @Override
    public void generate(GenMode mode, GameContext gameContext, OrderCalculator orderCalculator,
            BoardRepresentation board,
            Color side, MoveList moveList) {

//        generator.generate(board, side, moveList);

        switch (mode) {
        case NORMAL:
            generator.generate(board, side, moveList);
            break;
        case QUIESCENCE:
//            generator2.generate(board, side, moveList);

            generator2.generate(board, side, moveList, BBMoveGeneratorImpl2.GenTypes.CAPTURES);
            generator2.genPawnMoves(board.getBoard(), moveList, side, true);
            break;
        }

    }

}
