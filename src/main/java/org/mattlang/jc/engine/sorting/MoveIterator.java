package org.mattlang.jc.engine.sorting;

/**
 * Iterator over moves.
 */
public interface MoveIterator {

    /**
     * Returns true, if there is a next move available.
     *
     * @return
     */
    boolean hasNext();

    /**
     * Returns the next move. This is only available, if hasNext() returns true.
     *
     * @return
     */
    int next();

    /**
     * Returns the order of the current move.
     *
     * @return
     */
    int getOrder();
}
