package org.mattlang.jc.engine.sorting;

import java.util.logging.Logger;

import org.mattlang.jc.engine.MoveList;

/**
 * Simple move iterator implementation.
 */
public final class MoveIteratorImpl implements MoveIterator {

    public static final Logger LOGGER = Logger.getLogger(MoveIteratorImpl.class.getSimpleName());

    private MoveList moveList;

    /**
     * the remaining size of the picker. Note: this can be different from the size of the used move list in cases when
     * only a partial of the list should be iterated in staged usage.
     */
    private int size = 0;

    /**
     * the start of the picker.
     */
    private int start = 0;

    /**
     * current position of the picker.
     */
    private int current = -1;

    public MoveIteratorImpl() {
    }

    public MoveIteratorImpl(MoveList moveList, int start) {
        init(moveList, start);
    }

    public void init(MoveList moveList, int start) {
        init(moveList, start, moveList.size() - start);
    }

    public void init(MoveList moveList, int start, int size) {
        this.moveList = moveList;
        this.size = size;
        this.start = start;
        current = start - 1;
    }

    public boolean hasNext() {
        return size > 0;
    }

    /**
     * Does a lazy sorting of the rest of the list, searching the element with the lowest order and
     * putting the element with the lowest to the current position.
     *
     * @return the next move with the lowest order of all remaining moves.
     */
    public int next() {
        size--;
        current = start;
        start++;
        return moveList.get(current);
    }

    /**
     * returns the order of the current move.
     *
     * @return
     */
    public int getOrder() {
        return moveList.getOrder(current);
    }

}
