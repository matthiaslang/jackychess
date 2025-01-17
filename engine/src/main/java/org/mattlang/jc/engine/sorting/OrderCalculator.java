package org.mattlang.jc.engine.sorting;

import static java.util.Objects.requireNonNull;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.CounterMoveHeuristic;
import org.mattlang.jc.engine.search.HistoryHeuristic;
import org.mattlang.jc.engine.search.KillerMoves;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.see.SEE;
import org.mattlang.jc.moves.MoveImpl;

import lombok.Getter;

@Getter
public final class OrderCalculator {

    public static final int HASHMOVE_SCORE = -1500_000_000;
    public static final int GOOD_CAPTURES_SCORE = -100_000_000;

    public static final int KILLER_SCORE = -10_000_000;

    public static final int QUEEN_PROMOTION_SCORE = -80_000_000;

    public static final int COUNTER_MOVE_SCORE = -5_000_000;

    public static final int HISTORY_SCORE = -1_000_000;

    public static final int HISTORY_DIFF = 100_000;
    public static final int QUIET = -HISTORY_DIFF;

    public static final int BAD_CAPTURES_SCORE = -500_000;

    private static final int MVVLVA_MAX_DIFF = 500;

    private static final int GOOD_CAPT_LOWER = OrderCalculator.GOOD_CAPTURES_SCORE - MVVLVA_MAX_DIFF;
    private static final int GOOD_CAPT_UPPER = OrderCalculator.GOOD_CAPTURES_SCORE + MVVLVA_MAX_DIFF;

    private static final int BAD_CAPT_LOWER = OrderCalculator.BAD_CAPTURES_SCORE - MVVLVA_MAX_DIFF;
    private static final int BAD_CAPT_UPPER = OrderCalculator.BAD_CAPTURES_SCORE + MVVLVA_MAX_DIFF;

    private static final int HISTORY_LOWER = OrderCalculator.HISTORY_SCORE - HISTORY_DIFF;
    private static final int HISTORY_UPPER = OrderCalculator.HISTORY_SCORE + HISTORY_DIFF;

    final private HistoryHeuristic historyHeuristic;
    final private KillerMoves killerMoves;
    final private CounterMoveHeuristic counterMoveHeuristic;

    private Color color;

    private int ply;
    private final boolean useMvvLva;

    private int hashMove;
    private int parentMove;

    private BoardRepresentation board;

    private int captureMargin = 0;

    public OrderCalculator(SearchThreadContext stc) {
        this.historyHeuristic = requireNonNull(stc.getHistoryHeuristic());
        this.killerMoves = requireNonNull(stc.getKillerMoves());
        this.counterMoveHeuristic = requireNonNull(stc.getCounterMoveHeuristic());
        this.useMvvLva = Factory.getDefaults().getConfig().useMvvLvaSorting.getValue();
    }

    public void prepareOrder(Color color, final int hashMove, int parentMove, final int ply,
            BoardRepresentation board, int captureMargin) {

        this.hashMove = hashMove;
        this.parentMove = parentMove;

        this.ply = ply;
        this.color = color;
        this.board = board;
        this.captureMargin = captureMargin;
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
    private int calcOrder(MoveImpl m, int moveInt) {
        if (hashMove == moveInt) {
            return HASHMOVE_SCORE;
        } else if (m.isCapture()) {
            return calcOrderForCaptures(m);
        } else if (killerMoves.isKiller(moveInt, ply)) {
            return KILLER_SCORE;
        } else if (m.isQueenPromotion()) {
            return QUEEN_PROMOTION_SCORE;
        } else if (getCounterMove() == moveInt) {
            return COUNTER_MOVE_SCORE;
        } else {
            return calcOrderForQuiets(m);
        }
    }

    private int calcOrderForQuiets(MoveImpl m) {
        if (m.isQueenPromotion()) {
            return QUEEN_PROMOTION_SCORE;
        } else {
            // history heuristic
            int heuristic = historyHeuristic.calcValue(m, color);
            if (heuristic != 0) {
                // heuristic move: range: depth*depth*2*iterative-deep*moves ~ from 1 to 40000
                return -heuristic + HISTORY_SCORE;
            }
        }
        int mvvLva = useMvvLva ? MvvLva.calcMMVLVA(m) : 0;

        return -mvvLva * 1000 + QUIET;
    }

    private int calcOrderForCaptures(MoveImpl m) {
        if (m.isQueenPromotion()) {
            return QUEEN_PROMOTION_SCORE;
        } else if (m.isCapture()) {
            int mvvLva = useMvvLva ? MvvLva.calcMMVLVA(m) : 0;
            // find out good moves (via see)
            if (SEE.see_ge(board, m, captureMargin)) {
                return -mvvLva + GOOD_CAPTURES_SCORE;
            } else {
                return -mvvLva + BAD_CAPTURES_SCORE;
            }
        } else {
            throw new IllegalStateException("no capture!");
        }
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param moveList
     */
    public void scoreMoves(MoveList moveList) {
        scoreMoves(moveList, 0);
    }

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param moveList
     */
    public void scoreMoves(MoveList moveList, int start) {
        for (int i = start; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            moveList.setOrder(i, calcOrder(moveWrapper, moveInt));
        }
    }

    /**
     * Scores capture moves and returns the number of "good" captures.
     *
     * @param moveList
     * @param start
     * @return
     */
    public void scoreCaptureMoves(MoveList moveList, int start) {
        for (int i = start; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            int orderVal = calcOrderForCaptures(moveWrapper);
            moveList.setOrder(i, orderVal);
        }
    }

    public void scoreQuietMoves(MoveList moveList, int start) {
        for (int i = start; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            moveList.setOrder(i, calcOrderForQuiets(moveWrapper));
        }
    }

    /**
     * Returns an counter move for this position or 0.
     *
     * @return
     */
    public int getCounterMove() {
        return counterMoveHeuristic.getCounter(color.ordinal(), parentMove);
    }

    public static boolean isGoodCapture(int order) {
        return order > GOOD_CAPT_LOWER && order < GOOD_CAPT_UPPER;
    }

    public static boolean isBadCapture(int order) {
        return order > BAD_CAPT_LOWER && order < BAD_CAPT_UPPER;
    }

    public static boolean isGoodPromotion(int order) {
        return order == QUEEN_PROMOTION_SCORE;
    }

    public static boolean isHistory(int order) {
        return order > HISTORY_LOWER && order < HISTORY_UPPER;
    }

    public static boolean isHashMove(int order) {
        return order == HASHMOVE_SCORE;
    }

    public static boolean isKillerMove(int order) {
        return order == KILLER_SCORE;
    }

    public static boolean isCounterMove(int order) {
        return order == COUNTER_MOVE_SCORE;
    }

    /**
     * Is this a "relevant" move in the sense, that we have any kind of statistical relevance for this move?
     * So, is it either a hash move, good capture, killer, counter, or a move with move history statistics?
     *
     * This is used, to prune "non relevant" moves with certain criterias.
     *
     * @param order
     * @return
     */
    public static boolean isRelevantMove(int order) {
        return order < HISTORY_UPPER;
    }
}
