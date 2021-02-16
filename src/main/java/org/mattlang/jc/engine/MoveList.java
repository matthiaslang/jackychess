package org.mattlang.jc.engine;

import java.util.Comparator;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.movegenerator.MoveCollector;

public interface MoveList extends Iterable<MoveCursor>, MoveCollector {

    /**
     * Add move from another move cursor. This is used for filtering move lists.
     *
     * @param moveCursor
     */
    void addMove(MoveCursor moveCursor);

    /**
     * Sort move list by a comparator.
     * @param moveComparator
     */
    void sortMoves(Comparator<Move> moveComparator);

    int size();

}