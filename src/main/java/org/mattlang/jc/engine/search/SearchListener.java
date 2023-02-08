package org.mattlang.jc.engine.search;

/**
 * Listener to update about current search information, e.g. current move.
 */
public interface SearchListener {

    void updateCurrMove(int currMove, int savedMoveScore, int targetDepth, int selDepth, long nodesVisited);
}
