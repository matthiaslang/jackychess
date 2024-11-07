package org.mattlang.jc.perft;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MoveIteratorImpl;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;

/**
 * Simple recursive move iterator for perft tests.
 * It is simply based on movelists and board iterators using one stage for move generation.
 *
 * It therefore may have a different speed than the staged move generation which is used in the engine.
 * This supplier makes therefore only sense to test other aspects than real perft testing the real staged move
 * generation.
 */
public class SimplePerftIteratorSupplier implements PerftIteratorSupplier {

    private MoveList[] moveLists = new MoveList[MAX_PLY_INDEX];
    private MoveIteratorImpl[] iterators = new MoveIteratorImpl[MAX_PLY_INDEX];
    private MoveBoardIterator[] moveBoardIterators = new MoveBoardIterator[MAX_PLY_INDEX];

    private PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    public static final SimplePerftIteratorSupplier SIMPLE_PERFT_ITERATOR_SUPPLIER = new SimplePerftIteratorSupplier();

    public SimplePerftIteratorSupplier() {
        for (int i = 0; i < MAX_PLY_INDEX; i++) {
            moveLists[i] = new MoveList();
            iterators[i] = new MoveIteratorImpl();
            moveBoardIterators[i] = new MoveBoardIterator();
        }
    }

    @Override
    public MoveBoardIterator supplyIterator(int depth, BoardRepresentation board, Color color) {
        MoveList moveList = moveLists[depth];
        moveList.reset(color);

        generator.generate(GenMode.NORMAL, board, color, moveList);

        iterators[depth].init(moveList, 0);
        moveBoardIterators[depth].init(iterators[depth], board);
        return moveBoardIterators[depth];
    }
}
