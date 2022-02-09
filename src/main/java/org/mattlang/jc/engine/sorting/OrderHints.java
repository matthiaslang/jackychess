package org.mattlang.jc.engine.sorting;

import java.util.List;

import org.mattlang.jc.engine.search.*;

import lombok.AllArgsConstructor;

/**
 * Information or Hints to help order moves for the next round during iterative deepening.
 */
@AllArgsConstructor
public class OrderHints {

    public static final OrderHints NO_HINTS = new OrderHints(null, null, null, null, false);

    /**
     * an optional previous pv list of the last round. Could be null. If set, it should be used for move ordering.
     */
    public final PVList prevPvlist;

    /**
     * optional move scores of the last round of depth 1. If set, this should be used for move ordering for depth 1.
     */
    public final List<MoveScore> moveScores;

    public final HistoryHeuristic historyHeuristic;

    public final KillerMoves killerMoves;

    public final boolean useMvvLvaSorting;

    public OrderHints(
            NegaMaxResult negaMaxResult,
            SearchThreadContext stc ,
            boolean useMvvLvaSorting) {
        this.prevPvlist = negaMaxResult.pvList;
        this.moveScores = negaMaxResult.moveScores;
        this.historyHeuristic = stc.getHistoryHeuristic();
        this.killerMoves = stc.getKillerMoves();
        this.useMvvLvaSorting = useMvvLvaSorting;
    }
}
