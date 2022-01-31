package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

/**
 * a "pseude legal" move generator, means, not really a legal move generator by definition, but the contrary.
 * Since this seems far faster than a real legal move generator, this will probably the future code
 * and we should remove the whole legal move generator interface or use it only for test purpose...
 */
public class PseudoLegalMoveGenerator implements LegalMoveGenerator {

    BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        return moves;
    }
}
