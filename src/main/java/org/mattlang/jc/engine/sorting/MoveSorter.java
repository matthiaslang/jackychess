package org.mattlang.jc.engine.sorting;

import org.mattlang.jc.engine.MoveList;

/**
 * Sorts moves to optimize order for alpha/beta pruning (best moves first)
 */
public interface MoveSorter {

    /**
     * Sorts the move list and returns a sorted move list.
     * The returned move list could either be the same ordered, or a new created one which is sorted.
     *
     * @param movelist    unordered move list
     * @param orderHints  hints for sorting
     * @param depth       "working depth" from alpa/beta alg. starts from targetDepth down to 0
     * @param targetDepth the target depth of the current alpha/beta execution
     * @return
     */
    MoveList sort(MoveList movelist, final OrderHints orderHints, final int depth, int targetDepth);
}
