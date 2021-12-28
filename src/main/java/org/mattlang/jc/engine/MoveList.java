package org.mattlang.jc.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.moves.MoveImpl;

public interface MoveList extends Iterable<MoveCursor>, MoveCollector, AutoCloseable {

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
     * is this check mate for the current color? this is the case if no legal moves are available.
     * It may not be the same as size==0 if this is a filtered list.
     * @return
     */
    boolean isCheckMate();

    void setCheckMate(boolean checkMate);

    /**
     * override autoclosable close to not throw any exception.
     */
    void close();

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
        for (MoveCursor moveCursor : this) {
            l1.add(new MoveImpl(moveCursor.getMoveInt()));
        }
        l1.sort(Comparator.comparingInt(MoveImpl::toInt));
        return l1;
    }
}