package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.Constants.MAX_MOVES;

/**
 * Move picker which does a partial sort and picks the next move by its order priority from a move list.
 */
public final class MovePicker implements MoveIterator {

    private final int[] moves = new int[MAX_MOVES];
    private final int[] order = new int[MAX_MOVES];

    private int size = 0;
    /**
     * the start of the picker.
     */
    private int nextIndex = 0;

    /**
     * current position of the picker.
     */
    private int current = -1;

    private boolean fullySorted = false;

    public MovePicker() {
    }


    public void reset() {
        this.nextIndex = 0;
        current = -1;
        size = 0;
        fullySorted = false;
    }


    public void addMoveWithOrder(int aMove, int orderVal) {
        moves[size] = aMove;
        order[size] = orderVal;
        size++;
    }

    public boolean hasNext() {
        return nextIndex < size;
    }

    /**
     * Does a lazy sorting of the rest of the list, searching the element with the lowest order and
     * putting the element with the lowest to the current position.
     *
     * @return the next move with the lowest order of all remaining moves.
     */
    public int next() {
        sortRound();
        current = nextIndex;
        nextIndex++;
        return moves[current];
    }

    /**
     * returns the order of the current move.
     *
     * @return
     */
    public int getOrder() {
        return order[current];
    }

    private void sortRound() {
        if (fullySorted || nextIndex >= size - 1) {
            return;
        }

        // swap from back to front to move the highest element to the first place.
        // this is a stable sort that does not change order of equal entries.
        int swapCount = 0;
        for (int i = size - 2; i >= nextIndex; i--) {
            if (order[i] < order[i + 1]) {
                swap(i, i + 1);
                swapCount++;
            }
        }
        fullySorted = swapCount == 0;
    }

    private void swap(int i, int j) {

        int tmp = order[i];
        order[i] = order[j];
        order[j] = tmp;

        int ttmp = moves[i];
        moves[i] = moves[j];
        moves[j] = ttmp;
    }

}
