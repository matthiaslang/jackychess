package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveListToStringConverter;

/**
 * Move picker which does a partial sort and picks the next move by its order priority from a move list.
 */
public final class MovePicker implements MoveIterator {

    public static final Logger LOGGER = Logger.getLogger(MovePicker.class.getSimpleName());

    private MoveList moveList;

    /**
     * the remaining size of the picker. Note: this can be different from the size of the used move list in cases when
     * only a partial of the list should be iterated in staged usage.
     */
    private int size = 0;

    /**
     * the start of the picker.
     */
    private int start = 0;

    /**
     * current position of the picker.
     */
    private int current = -1;

    public MovePicker() {
    }

    public MovePicker(MoveList moveList, int start) {
        init(moveList, start);
    }

    public void init(MoveList moveList, int start) {
        init(moveList, start, moveList.size() - start);
    }

    public void init(MoveList moveList, int start, int size) {
        this.moveList = moveList;
        this.size = size;
        this.start = start;
        current = start - 1;

        if (BuildConstants.ASSERTIONS) {
            validate();
        }
    }

    /**
     * debug validation consistency checks.
     */
    private void validate() {
        if (start + size > moveList.size()) {
            throw new IllegalStateException("picker too long!");
        }
        if (start > 0) {
            // staged usage, where alredy moves have been chosen. this means all moves before start must be already properly ordered:
            int currOrder = Integer.MIN_VALUE;

            for (int i = 0; i < start; i++) {
                if (moveList.getOrder(i) < currOrder) {
                    throw new IllegalStateException("inconsistent order of previous chosen moves!");
                }
                currOrder = moveList.getOrder(i);
            }
            // it means also that all moves beyond start should have a lower prio order (higher value) than the last used one:
            // only exception is a quiet queen promotion which has a rel. high value (higher than most good captures) but is part
            // of the quiet staging phase:
            for (int i = start; i < moveList.size(); i++) {

                if (moveList.getOrder(i) <= currOrder
                        && moveList.getOrder(i) != OrderCalculator.QUEEN_PROMOTION_SCORE) {
                    throw new IllegalStateException(
                            "inconsistent order of following moves. They should have been chosen earlier!");
                }
            }
        }

        List<String> moves = MoveListToStringConverter.toString(moveList, 0, start + size);
        LOGGER.info("picker prepared " + size + " moves, starting at " + start);
        LOGGER.info("move list order: " + moves);
    }

    private List<String> createMoveDebugOrders(ArrayList<Integer> moveOrders) {
        return moveOrders.stream()
                .map(o -> mapDebugOrderStr(o))
                .collect(Collectors.toList());
    }

    public static String mapDebugOrderStr(Integer o) {
        int oi = o.intValue();
        if (oi == OrderCalculator.HASHMOVE_SCORE) {
            return "1.HASH";
        }
        if (OrderCalculator.isGoodCapture(oi)) {
            return "2.GOOD CAP";
        }
        if (OrderCalculator.isGoodPromotion(oi)) {
            return "3.GOOD PROM";
        }
        if (OrderCalculator.isKillerMove(oi)) {
            return "4.KILLER";
        }
        if (OrderCalculator.isCounterMove(oi)) {
            return "5.COUNTER";
        }
        if (OrderCalculator.isHistory(oi)) {
            return "6.HISTORY";
        }
        if (OrderCalculator.isBadCapture(oi)) {
            return "8.BAD CAP";
        }
        return "7.QUIET";
    }

    public boolean hasNext() {
        return size > 0;
    }

    /**
     * Does a lazy sorting of the rest of the list, searching the element with the lowest order and
     * putting the element with the lowest to the current position.
     *
     * @return the next move with the lowest order of all remaining moves.
     */
    public int next() {
        sortRound();
        size--;
        current = start;
        start++;
        return moveList.get(current);
    }

    public int getCurrentIndex() {
        return current;
    }

    /**
     * returns the order of the current move.
     *
     * @return
     */
    public int getOrder() {
        return moveList.getOrder(current);
    }

    private void sortRound() {
        if (start >= moveList.size() - 1) {
            return;
        }
        int currLowest = -1;
        int currLowestIndex = start;
        for (int i = start; i < moveList.size(); i++) {
            if (moveList.getOrder(i) < currLowest || currLowest == -1) {
                currLowest = moveList.getOrder(i);
                currLowestIndex = i;
            }
        }

        if (currLowestIndex != start) {
            moveList.swap(start, currLowestIndex);
        }
    }



}
