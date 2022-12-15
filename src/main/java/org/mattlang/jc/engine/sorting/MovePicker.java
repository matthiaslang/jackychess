package org.mattlang.jc.engine.sorting;

import org.mattlang.jc.moves.MoveListImpl;

/**
 * Move picker which does a partial sort and picks the next move by its order priority from a move list.
 */
public final class MovePicker {

    private MoveListImpl moveList;

    private int size = 0;

    private int start = 0;

    private int current = -1;

    private int swapCounter = 0;
    private boolean alreadyFullySorted = false;

    public MovePicker() {
    }

    public MovePicker(MoveListImpl moveList, int start) {
        init(moveList, start);
    }

    public void init(MoveListImpl moveList, int start) {
        this.moveList = moveList;
        this.size = moveList.size();
        this.start = start;
        current = start - 1;
        swapCounter = 0;
        alreadyFullySorted = false;
    }

    private void sortData() {
        while (!alreadyFullySorted) {
            sortRound();
        }
    }

    public boolean hasNext() {
        return size > 0;
    }

    public int next() {
        if (!alreadyFullySorted) {
            sortRound();
        }
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
        if (start >= size - 1) {
            return;
        }
        swapCounter = 0;
        int currLowest = -1;
        int currLowestIndex = -1;
        for (int i = start; i < size - 1; i++) {
            if (moveList.getOrder(i) > moveList.getOrder(i + 1)) {
                swap(i, i + 1);

            }
            if (moveList.getOrder(i) < currLowest || currLowest == -1) {
                currLowest = moveList.getOrder(i);
                currLowestIndex = i;
            }

        }

        if (currLowestIndex != start) {
            swap(start, currLowestIndex);
        }
        if (swapCounter == 0) {
            alreadyFullySorted = true;
        }
    }

    private void swap(int i, int j) {
        swapCounter++;
        moveList.swap(i, j);
    }
}
