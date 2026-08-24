package org.mattlang.jc.engine.sorting;

import static java.util.Objects.requireNonNull;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.*;
import org.mattlang.jc.engine.see.SEE;
import org.mattlang.jc.moves.MoveImpl;

import lombok.Getter;

@Getter
public final class OrderCalculator {

    public static final int O01 = 1 << 30;
    public static final int O02 = 1 << 29;
    public static final int O03 = 1 << 28;
    public static final int O04 = 1 << 27;
    public static final int O05 = 1 << 26;
    public static final int O06 = 1 << 25;
    public static final int O07 = 1 << 24;
    public static final int O08 = 1 << 23;


    public static final int HASHMOVE_SCORE = O01;
    public static final int GOOD_CAPTURES_SCORE = O02;
    public static final int QUEEN_PROMOTION_SCORE = O03;

    public static final int KILLER_SCORE = O04;

    public static final int COUNTER_MOVE_SCORE = O05;

    public static final int HISTORY_SCORE = O06;

    public static final int RELEVANT_MOVE_MASK = HASHMOVE_SCORE | GOOD_CAPTURES_SCORE
            | QUEEN_PROMOTION_SCORE
            | KILLER_SCORE | COUNTER_MOVE_SCORE | HISTORY_SCORE;

    // test: bad captures before normal quiets
    public static final int BAD_CAPTURES_SCORE = O07;
    public static final int QUIET = O08;

    final private HistoryHeuristic historyHeuristic;
    final private KillerMoves killerMoves;
    final private CounterMoveHeuristic counterMoveHeuristic;
    private final CaptureHeuristic captureHeuristic;
    private final ContinuationHistoryHeuristic continuationHistoryHeuristic;

    private int color;

    private int ply;

    private int hashMove;
    private int parentMove;

    private BoardRepresentation board;

    private int captureMargin = 0;

    public OrderCalculator(SearchThreadContext stc) {
        this.historyHeuristic = requireNonNull(stc.getHistoryHeuristic());
        this.captureHeuristic = requireNonNull(stc.getCaptureHeuristic());
        this.continuationHistoryHeuristic = requireNonNull(stc.getContinuationHistoryHeuristic());
        this.killerMoves = requireNonNull(stc.getKillerMoves());
        this.counterMoveHeuristic = requireNonNull(stc.getCounterMoveHeuristic());
    }

    public void prepareOrder(int color, final int hashMove, int parentMove, final int ply,
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
     * <p>
     * best: pv
     * <p>
     * then good captures
     * <p>
     * then killer moves
     * <p>
     * then history heuristic
     * <p>
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
        } else if (m.isPromotion()) {
            int score = MvvLva.calcMMVLVAShort(m);
            score += BAD_CAPTURES_SCORE;
            return score;
        } else if (getCounterMove() == moveInt) {
            return COUNTER_MOVE_SCORE;
        } else {
            return calcOrderForQuiets(m);
        }
    }

    private int calcOrderForQuiets(MoveImpl m) {
        if (m.isQueenPromotion()) {
            return QUEEN_PROMOTION_SCORE;
        } else if (m.isPromotion()) {
            int score = MvvLva.calcMMVLVAShort(m);
            score += BAD_CAPTURES_SCORE;
            return score;
        } else {

            int score = 0;
            // history heuristic
            int heuristic = historyHeuristic.calcValue(m, color);
            int contHist = continuationHistoryHeuristic.calcValue(parentMove, m, color);
            score += heuristic;
            score += contHist;

            if (score != 0) {
                return score + HISTORY_SCORE;
            }
        }
        /**
         * sort at least by figuretype, ordererd from pawn... -> queen. this gives in tests a little reduced
         * search tree.
         */
        return m.getFigureType() + QUIET;
    }

    private static int[] MVVAUGMENT = {0, 100, 320, 330, 500, 900, 0};

    private int calcOrderForCaptures(MoveImpl m) {
        if (m.isQueenPromotion()) {
            return QUEEN_PROMOTION_SCORE;
        } else if (m.isCapture()) {
            int score = MvvLva.calcMMVLVAShort(m);

            int heuristic = captureHeuristic.calcValue(m, color);

            score += heuristic / 4;

            int captFigType = m.getCapturedFigure() & MASK_OUT_COLOR;
            score += MVVAUGMENT[captFigType];

            boolean goodSee = SEE.see_ge(board, m, captureMargin);
            if (goodSee) {
                score += GOOD_CAPTURES_SCORE;
            } else {
                score += BAD_CAPTURES_SCORE;
            }

            return score;
        } else if (m.isPromotion()) {
            int score = MvvLva.calcMMVLVAShort(m);
            score += BAD_CAPTURES_SCORE;
            return score;
        } else {
            throw new IllegalStateException("no capture!");
        }
    }

    private final MoveImpl moveWrapper = new MoveImpl("a1a2");

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param moveList
     */
    public void scoreMoves(MoveList moveList,  MovePicker goods, MovePicker bads) {
        for (int i = 0; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            int orderVal = calcOrder(moveWrapper, moveInt);
            if (isRelevantMove(orderVal)) {
                goods.addMoveWithOrder(moveInt, orderVal);
            } else {
                bads.addMoveWithOrder(moveInt, orderVal);
            }
        }
    }

    /**
     * Scores capture moves and returns the number of "good" captures.
     *
     * @param moveList
     * @param start
     * @return
     */
    public void scoreCaptureMoves(MoveList moveList, MovePicker goods, MovePicker bads) {
        for (int i = 0; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            int orderVal = calcOrderForCaptures(moveWrapper);
            if (isRelevantMove(orderVal)) {
                goods.addMoveWithOrder(moveInt, orderVal);
            } else {
                bads.addMoveWithOrder(moveInt, orderVal);
            }
        }
    }

    public void scoreQuietMoves(MoveList moveList, MovePicker goods, MovePicker bads) {
        for (int i = 0; i < moveList.size(); i++) {
            int moveInt = moveList.get(i);
            moveWrapper.fromLongEncoded(moveInt);
            int orderVal = calcOrderForQuiets(moveWrapper);
            if (isRelevantMove(orderVal)) {
                goods.addMoveWithOrder(moveInt, orderVal);
            } else {
                bads.addMoveWithOrder(moveInt, orderVal);
            }
        }
    }

    /**
     * Returns an counter move for this position or 0.
     *
     * @return
     */
    public int getCounterMove() {
        return counterMoveHeuristic.getCounter(color, parentMove);
    }

    public static boolean isGoodCapture(int order) {
        return (order & GOOD_CAPTURES_SCORE) != 0;
    }

    public static boolean isBadCapture(int order) {
        return (order & BAD_CAPTURES_SCORE) != 0;
    }

    public static boolean isGoodPromotion(int order) {
        return (order & QUEEN_PROMOTION_SCORE) != 0;
    }

    public static boolean isHistory(int order) {
        return (order & HISTORY_SCORE) != 0;
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
     * <p>
     * This is used, to prune "non relevant" moves with certain criterias.
     *
     * @param order
     * @return
     */
    public static boolean isRelevantMove(int order) {
        return (order & RELEVANT_MOVE_MASK) != 0;
    }
}
