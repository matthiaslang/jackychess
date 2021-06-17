package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.engine.evaluation.Weights.PAWN_WEIGHT;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;

public class OrderCalculator {

    private static final int PV_SCORE = -(1 << 30);
    private static final int GOOD_CAPTURES_SCORE = -(1 << 26);
    private static final int KILLER_SCORE = -(1 << 24);
    private static final int HISTORY_SCORE = -(1 << 23);
    private static final int BAD_CAPTURES_SCORE = -(1 << 22);

    private final Move pvMove;
    private final HistoryHeuristic historyHeuristic;
    private final KillerMoves killerMoves;
    private final Color color;

    private final int depth;
    private final int ply;
    private final boolean useMvvLva;
    private final Boolean usePvSorting;

    public OrderCalculator(final OrderHints orderHints, Color color,
            final int depth, int targetDepth) {
        this.ply = targetDepth - depth;
        this.pvMove = orderHints.prevPvlist != null ? orderHints.prevPvlist.get(ply) : null;
        this.historyHeuristic = orderHints.historyHeuristic;
        this.killerMoves = orderHints.killerMoves;
        this.color = color;
        this.depth = depth;
        this.useMvvLva = orderHints.useMvvLvaSorting;

        this.usePvSorting = Factory.getDefaults().getConfig().usePvSorting.getValue();
    }

    /**
     * Calc sort order:
     *
     * best: pv
     *
     * then good captures
     *
     * then killer moves
     *
     * then history heuristic
     *
     * then bad captures
     *
     * @param m
     * @return
     */
    public int calcOrder(Move m) {
        if (usePvSorting && pvMove != null && pvMove.equals(m)) {
            return PV_SCORE;
        } else {
            int mvvLva = MvvLva.calcMMVLVA(m);
            // mvvLva = 500 best - -500 worst
            if (m.isCapture()) {
                if (useMvvLva) {
                    // good captures. we should more fine grain distinguish "good"
                    if (mvvLva >= PAWN_WEIGHT) {
                        // good capture [100000-800000]
                        return -mvvLva + GOOD_CAPTURES_SCORE;
                    } else {
                        // bad capture [0-500]
                        return -mvvLva + BAD_CAPTURES_SCORE;
                    }
                } else {
                    return 0;
                }
            } else if (killerMoves != null && killerMoves.isKiller(color, m, ply)) {
                return KILLER_SCORE;
            } else if (historyHeuristic != null) {
                // history heuristic
                int heuristic = historyHeuristic.calcValue(m, color);
                if (heuristic != 0) {
                    // heuristic move: range: depth*depth*2*iterative-deep*moves ~ from 1 to 40000
                    return -heuristic + HISTORY_SCORE;
                }
            }
            // otherwise same as "bad move". a negative value, this should then be sorted at latest
            return useMvvLva ? -mvvLva + BAD_CAPTURES_SCORE : 0;
        }

    }

}
