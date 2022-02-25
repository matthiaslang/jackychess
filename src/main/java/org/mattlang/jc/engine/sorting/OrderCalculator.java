package org.mattlang.jc.engine.sorting;

import java.util.HashMap;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.CounterMoveHeuristic;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;
import org.mattlang.jc.engine.search.MoveScore;
import org.mattlang.jc.engine.see.SEE;

import lombok.Getter;

@Getter
public class OrderCalculator {

    public static final int HASHMOVE_SCORE = -1500_000_000;
    public static final int PV_SCORE = -1000_000_000;
    public static final int GOOD_CAPTURES_SCORE = -100_000_000;

    public static final int KILLER_SCORE = -10_000_000;

    public static final int COUNTER_MOVE_SCORE = -5_000_000;

    public static final int HISTORY_SCORE = -1_000_000;

    public static final int LATE_MOVE_REDUCTION_BORDER = 0;

    /**
     * a good capture is where I earn (statically viewed) at least a lower figure or more.
     */
    private static final int GOOD_CAPTURE_WEIGHT = 15;

    private int pvMove;
    private final HistoryHeuristic historyHeuristic;
    private final KillerMoves killerMoves;
    private final CounterMoveHeuristic counterMoveHeuristic;
    private Color color;

    private int depth;
    private int ply;
    private final boolean useMvvLva;
    private final Boolean usePvSorting;

    private HashMap<Integer, Integer> scores;
    private final int targetDepth;
    private final OrderHints orderHints;

    private int hashMove;
    private int parentMove;

    private BoardRepresentation board;

    private static SEE see = new SEE();

    public OrderCalculator(OrderHints orderHints, int targetDepth) {
        this.orderHints = orderHints;
        this.targetDepth = targetDepth;
        this.historyHeuristic = orderHints.historyHeuristic;
        this.killerMoves = orderHints.killerMoves;
        this.counterMoveHeuristic = orderHints.counterMoveHeuristic;
        this.useMvvLva = orderHints.useMvvLvaSorting;
        this.usePvSorting = Factory.getDefaults().getConfig().usePvSorting.getValue();
    }

    public void prepareOrder(Color color, final int hashMove, int parentMove, final int ply, final int depth,
            BoardRepresentation board) {

        this.hashMove = hashMove;
        this.parentMove = parentMove;
        int index = ply - 1;
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

        this.ply = ply;
        this.pvMove = orderHints.prevPvlist != null ? orderHints.prevPvlist.getMove(index) : 0;
        this.color = color;
        this.depth = depth;
        this.board = board;
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
        int moveInt = m.toInt();
        if (scores != null) {
            Integer rslt = scores.get(moveInt);
            if (rslt == null){
               // UCILogger.log("hey!! this should not happen!!!! cant find scores for " + m.toStr());
                // this could happen for pseudo legal moves for illegal moves: here we have no score from the last round
                return 0;
            }
            return -rslt;
        }

        if (usePvSorting && pvMove == moveInt) {
            return PV_SCORE;
        } else if (hashMove == moveInt) {
            return HASHMOVE_SCORE;
        } else {
            int mvvLva = useMvvLva ? MvvLva.calcMMVLVA(m) : 0;

            if (m.isCapture()) {
                // find out good moves (via see)
                if (see.see_ge(board, m, 0)) {
                    return -mvvLva + GOOD_CAPTURES_SCORE;
                } else {
                    return -mvvLva;
                }

            } else if (killerMoves != null && killerMoves.isKiller(color, moveInt, ply)) {
                return KILLER_SCORE;
            } else if (counterMoveHeuristic != null
                    && counterMoveHeuristic.getCounter(color.ordinal(), parentMove) == moveInt) {
                return COUNTER_MOVE_SCORE;
            } else if (historyHeuristic != null) {
                // history heuristic
                int heuristic = historyHeuristic.calcValue(m, color);
                if (heuristic != 0) {
                    // heuristic move: range: depth*depth*2*iterative-deep*moves ~ from 1 to 40000
                    return -heuristic + HISTORY_SCORE;
                }
            }
            return -mvvLva;
        }

    }

}
