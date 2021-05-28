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