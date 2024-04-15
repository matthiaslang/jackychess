package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Move;

/**
 * Listener to update about iterative deeping results.
 */
public interface IterativeDeepeningListener {

    /**
     * Updates the listener about a new bestmove after finishing an iterative round.
     *
     * @param bestMove
     */
    void updateBestRoundMove(Move bestMove);
}
