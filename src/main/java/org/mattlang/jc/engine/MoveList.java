package org.mattlang.jc.engine;

import java.util.Comparator;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveCollector;

public interface MoveList extends Iterable<MoveCursor>, MoveCollector {

    /**
     * Sort move list by a comparator.
     * @param moveComparator
     */
    @Deprecated
    void sortMoves(Comparator<Move> moveComparator);

    /**
     * sort the list with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     * The sort algorithm is implementation detail. the move list can e.g. use lazy sorting
     * @param orderCalculator
     */
     void sort(OrderCalculator orderCalculator);

    /**
     * the no of moves in this list.
     * @return
     */
    int size();

    /**
     * is this a subset (a filtered set) of moves?
     * @return
     */
    boolean isSubset();

    /**
     * are these legal moves ore pseudo legal moves?
     */
    boolean isLegal();

    /**
     * is this check mate for the current color? this is the case if no legal moves are available.
     * It may not be the same as size==0 if this is a filtered list.
     * @return
     */
    boolean isCheckMate();

    void setSubset(boolean subset);

    void setLegal(boolean legal);

    void setCheckMate(boolean checkMate);

}