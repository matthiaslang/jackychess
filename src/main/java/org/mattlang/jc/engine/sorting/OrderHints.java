package org.mattlang.jc.engine.sorting;

import java.util.List;

import org.mattlang.jc.engine.search.MoveScore;
import org.mattlang.jc.engine.search.PVList;

/**
 * Information or Hints to help order moves for the next round during iterative deepening.
 */
public class OrderHints {

    public static final OrderHints NO_HINTS = new OrderHints(null, null);

    /**
     * an optional previous pv list of the last round. Could be null. If set, it should be used for move ordering.
     */
    public final PVList prevPvlist;

    /**
     * optional move scores of the last round of depth 1. If set, this should be used for move ordering for depth 1.
     */
    public final List<MoveScore> moveScores;

    public OrderHints(PVList prevPvlist, List<MoveScore> moveScores) {
        this.prevPvlist = prevPvlist;
        this.moveScores = moveScores;
    }
}
