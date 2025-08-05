package org.mattlang.jc.engine.search;

/**
 * Listener to update about iterative deeping results.
 */
public interface IterativeDeepeningListener {

    /**
     * Updates the listener about a new bestmove after finishing an iterative round.
     *
     * @param bestRoundResult
     */
    void updateBestRoundMove(NegaMaxResult bestRoundResult);
}
