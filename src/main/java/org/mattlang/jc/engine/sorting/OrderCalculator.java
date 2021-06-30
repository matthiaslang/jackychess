package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.engine.evaluation.Weights.PAWN_WEIGHT;

import java.util.HashMap;

import org.mattlang.jc.Factory;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.PawnPromotionMove;
import org.mattlang.jc.board.RochadeMove;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;
import org.mattlang.jc.engine.search.MoveScore;

public class OrderCalculator {

    private static final int PV_SCORE = -1000_000_000;
    private static final int GOOD_CAPTURES_SCORE = -100_000_000;
    private static final int GOOD_PROMOTIONS = -90_000_000;

    private static final int KILLER_SCORE = -10_000_000;
    private static final int HISTORY_SCORE = -1_000_000;
    private static final int BAD_CAPTURES_SCORE = -100_000;
    private static final int CASTLING_SCORE = -20_000;
    private static final int QUIET_MOVE_SCORE = -10_000;

    /** a good capture is where I earn (statically viewed) at least 2 pawn weight. */
    private static final int GOOD_CAPTURE_WEIGHT = PAWN_WEIGHT * 2;

    private final Move pvMove;
    private final HistoryHeuristic historyHeuristic;
    private final KillerMoves killerMoves;
    private final Color color;

    private final int depth;
    private final int ply;
    private final boolean useMvvLva;
    private final Boolean usePvSorting;

    private final HashMap<Move, Integer> scores;

    public OrderCalculator(final OrderHints orderHints, Color color,
            final int depth, int targetDepth) {

        int index = targetDepth - depth;
        // if we are at the root and have scores from a previous run, lets take them:
        if (index == 0 && orderHints.moveScores != null) {
            // todo assert that the first pvs should be the highest score...
            scores = new HashMap<>();
            for (MoveScore moveScore : orderHints.moveScores) {
                scores.put(moveScore.move, moveScore.score);
            }

        } else {
            scores = null;
        }

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
        if (scores != null) {
            Integer rslt = scores.get(m);
            if (rslt == null){
                UCILogger.log("hey!! this should not happen!!!! cant find scores for " + m.toStr());
                return 0;
            }
            return -rslt;
        }

        if (usePvSorting && pvMove != null && pvMove.equals(m)) {
            return PV_SCORE;
        } else {
            int mvvLva = MvvLva.calcMMVLVA(m);
            // for now do not distinguish different promotions, but captures:
            if (m instanceof PawnPromotionMove) {
                return -mvvLva + GOOD_PROMOTIONS;
            } else if (m.isCapture()) {
                // mvvLva = 500 best - -500 worst
                if (useMvvLva) {
                    // good captures. we should more fine grain distinguish "good"
                    if (mvvLva >= GOOD_CAPTURE_WEIGHT) {
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

            if (m instanceof RochadeMove){
                return CASTLING_SCORE;
            }
            // otherwise a quiet move:
            return useMvvLva ? -mvvLva + QUIET_MOVE_SCORE : 0;
        }

    }

}
