package org.mattlang.jc.engine.sorting;

import java.util.Comparator;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.PVList;

public class SimpleMoveComparator implements Comparator<Move> {

    private final Move pvMove;
    private final HistoryHeuristic historyHeuristic;
    private final Color color;

    public SimpleMoveComparator(final PVList prevPvlist, Color color,
            HistoryHeuristic historyHeuristic, final int depth, int targetDepth) {
        int index = targetDepth - depth;
        pvMove = prevPvlist != null ? prevPvlist.get(index) : null;
        this.historyHeuristic = historyHeuristic;
        this.color = color;
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (o1.getOrder() == 0) {
            o1.setOrder(calcPVCmp(o1));
        }
        if (o2.getOrder() == 0) {
            o2.setOrder(calcPVCmp(o2));
        }

        int cmp = o1.getOrder() - o2.getOrder();
        return cmp;
    }

    /**
     * Calc sort order:
     *
     * best: pv
     *
     * then good captures
     *
     * then history heuristic
     *
     * then bad captures
     *
     * @param m
     * @return
     */
    private int calcPVCmp(Move m) {
        if (pvMove != null && pvMove.equals(m)) {
            return -10000000;
        } else {
            short mvvLva = m.getMvvLva();
            // mvvLva = 500 best - -500 worst

            // good captures. we should more fine grain distinguish "good"

            if (m.isCapture()) {
                if (mvvLva >= 100) {
                    // good move [100000-500000]
                    return -mvvLva * 1000;
                } else {
                    // bad move [0-500]
                    return -mvvLva;
                }
            } else if (historyHeuristic != null) {
                // history heuristic
                int heuristic = historyHeuristic.calcValue(m, color);
                if (heuristic != 0) {
                    // heuristic move: range: depth*depth*2*iterative-deep*moves ~ from 1 to 40000
                    return -heuristic;
                }
            }
        }
        // otherwise same as "bad move". a negative value, this should then be sorted at latest
        return -m.getMvvLva();
    }


}
