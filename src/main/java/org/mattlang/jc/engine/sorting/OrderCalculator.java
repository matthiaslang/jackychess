package org.mattlang.jc.engine.sorting;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.CaptureHeuristic;
import org.mattlang.jc.engine.search.CounterMoveHeuristic;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;
import org.mattlang.jc.engine.see.SEE;

import lombok.Getter;

@Getter
public class OrderCalculator {

    public static final int HASHMOVE_SCORE = -1500_000_000;
    public static final int PV_SCORE = -1000_000_000;
    public static final int GOOD_CAPTURES_SCORE = -100_000_000;

    public static final int KILLER_SCORE = -10_000_000;

    public static final int QUEEN_PROMOTION_SCORE = -80_000_000;

    public static final int COUNTER_MOVE_SCORE = -5_000_000;

    public static final int HISTORY_SCORE = -1_000_000;

    public static final int QUIET = -100_000;

    public static final int BAD_CAPTURES_SCORE = -500_000;

    public static final int LATE_MOVE_REDUCTION_BORDER = 0;

    private static final int GOOD_CAPT_LOWER = OrderCalculator.GOOD_CAPTURES_SCORE - 1000000;
    private static final int GOOD_CAPT_UPPER = OrderCalculator.GOOD_CAPTURES_SCORE + 1000000;

    private int pvMove;
    private HistoryHeuristic historyHeuristic;
    private CaptureHeuristic captureHeuristic;
    private KillerMoves killerMoves;
    private CounterMoveHeuristic counterMoveHeuristic;
    private Color color;

    private int ply;
    private boolean useMvvLva;
    private Boolean usePvSorting;

    private OrderHints orderHints;

    private int hashMove;
    private int parentMove;

    private BoardRepresentation board;

    private static SEE see = new SEE();

    public OrderCalculator(OrderCalculator orderCalculator) {
        init(orderCalculator);
    }

    /**
     * used in staged move gen, because since the order calculator keeps state, we need to copy the state
     * in staged move gen, since the stages are iteratively generated and mixed with recursive search and would
     * collide with other stage move gens and the same order calculator instance.
     *
     * @param other
     * @todo maye be we should have an own order calculator for each move list instance...
     */
    public void init(OrderCalculator other) {
        this.orderHints = other.orderHints;
        this.historyHeuristic = other.historyHeuristic;
        this.killerMoves = other.killerMoves;
        this.counterMoveHeuristic = other.counterMoveHeuristic;
        this.captureHeuristic = other.captureHeuristic;
        this.useMvvLva = orderHints.useMvvLvaSorting;
        this.usePvSorting = other.usePvSorting;

        this.hashMove = other.hashMove;
        this.parentMove = other.parentMove;

        this.ply = other.ply;
        this.pvMove = other.pvMove;
        this.color = other.color;
    }

    public OrderCalculator(OrderHints orderHints) {
        this.orderHints = orderHints;
        if (orderHints.stc != null) {
            this.historyHeuristic = orderHints.stc.getHistoryHeuristic();
            this.captureHeuristic = orderHints.stc.getCaptureHeuristic();
            this.killerMoves = orderHints.stc.getKillerMoves();
            this.counterMoveHeuristic = orderHints.stc.getCounterMoveHeuristic();
        }
        this.useMvvLva = orderHints.useMvvLvaSorting;
        this.usePvSorting = Factory.getDefaults().getConfig().usePvSorting.getValue();
    }

    public void prepareOrder(Color color, final int hashMove, int parentMove, final int ply,
            BoardRepresentation board) {

        this.hashMove = hashMove;
        this.parentMove = parentMove;
        int index = ply - 1;

        this.ply = ply;
        this.pvMove = orderHints.prevPvlist != null ? orderHints.prevPvlist.getMove(index) : 0;
        this.color = color;
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
        int moveInt = m.getMoveInt();
        if (usePvSorting && pvMove == moveInt) {
            return PV_SCORE;
        } else if (hashMove == moveInt) {
            return HASHMOVE_SCORE;
        } else {
            int mvvLva = useMvvLva ? MvvLva.calcMMVLVA(m) : 0;

            if (m.isCapture()) {
                // find out good moves (via see)
                int captScore = 0;
                if (see.see_ge(board, m, 0)) {
                    captScore = -mvvLva + GOOD_CAPTURES_SCORE;
                } else {
                    captScore = -mvvLva + BAD_CAPTURES_SCORE;
                }
                if (captureHeuristic != null) {
                    captScore -= captureHeuristic.calcValue(m, color);
                }
                return captScore;

            } else if (killerMoves != null && killerMoves.isKiller(color, moveInt, ply)) {
                return KILLER_SCORE;
            } else if (m.isPromotion() && m.getPromotedFigure().figureType == FigureType.Queen) {
                return QUEEN_PROMOTION_SCORE;
            } else if (getCounterMove() == moveInt) {
                return COUNTER_MOVE_SCORE;
            } else if (historyHeuristic != null) {
                // history heuristic
                int heuristic = historyHeuristic.calcValue(m, color);
                if (heuristic != 0) {
                    // heuristic move: range: depth*depth*2*iterative-deep*moves ~ from 1 to 40000
                    return -heuristic + HISTORY_SCORE;
                }
            }
            return -mvvLva + QUIET;
        }

    }

    /**
     * Returns an counter move for this position or 0.
     *
     * @return
     */
    public int getCounterMove() {
        return counterMoveHeuristic != null ? counterMoveHeuristic.getCounter(color.ordinal(), parentMove) : 0;
    }

    public static boolean isGoodCapture(int order) {
        return order > GOOD_CAPT_LOWER;
    }

    public boolean hasPvMove(int ply){
        int index=ply-1;
        return orderHints.prevPvlist != null && orderHints.prevPvlist.getMove(index) != 0;
    }
}
