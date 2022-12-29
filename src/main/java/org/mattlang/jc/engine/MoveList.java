package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface MoveList extends MoveCollector {

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param orderCalculator
     */
    void scoreMoves(OrderCalculator orderCalculator);

    /**
     * the no of moves in this list.
     * @return
     */
    int size();

    /**
     * Extracts a java util ist with all moves a move objects.
     * This is not used during search as it would be too slow.
     * Its only for debugging purpose in test code since this is more handy
     * to inspect than an encoded int list.
     *
     * @return
     */
    default List<MoveImpl> extractList() {
        ArrayList<MoveImpl> l1 = new ArrayList<>();
        MoveCursor moveCursor=iterate();
        while (moveCursor.hasNext()) {
            moveCursor.next();
            l1.add(new MoveImpl(moveCursor.getMoveInt()));
        }
        l1.sort(Comparator.comparingInt(MoveImpl::getMoveInt));
        return l1;
    }

    void reset();

    /**
     * Returns a cursor initialized for iteration.
     * Note: due ot performance and memory usage, there is only one cursor associated with a move list.
     * So the caller can not use more than one iteration at one time.
     *
     * @return
     */
    MoveCursor iterate();

    /**
     * Returns a cursor doing/undoing moves on a board. Only legal moves will be iterated, illegal moves are skipped
     * by this iterator.
     * Note: due ot performance and memory usage, there is only one cursor and MoveBoardIterator associated with a move list.
     * So the caller can not use more than one iteration at one time.
     *
     * @param board
     * @param checkChecker
     * @return
     */
    MoveBoardIterator iterateMoves(BoardRepresentation board, CheckChecker checkChecker);
}