package org.mattlang.jc.engine.sorting;

/**
 * Sorts objects by a separate orders array.
 * Does this lazy, to safe time when e.g. not all moves are needed when a cut off happened.
 * Kind of lazy merge sort to prevent sorting the complete array.
 */
public class LongSorter {

    private int[] objects;
    private int[] orders;

    private int size = 0;

    private int start = 0;

    public LongSorter(int[] objects, int size, int[] orders) {
        init(objects, size, orders);
    }

    public void init(int[] objects, int size, int[] orders) {
        this.objects = objects;
        this.orders = orders;
        this.size = size;
        start = 0;
    }

    public boolean hasNext() {
        return start < size;
    }

    public int next() {
        sortRound();
        return objects[start++];
    }

    /**
     * returns the order of the current move.
     * @return
     */
    public int getOrder() {
        return orders[start];
    }

    private void sortRound() {
        int currLowest = -1;
        int currLowestIndex = start;
        for (int i = start; i < size; i++) {
            if (orders[i] < currLowest || currLowest == -1) {
                currLowest = orders[i];
                currLowestIndex = i;
            }
        }

        if (currLowestIndex != start && currLowestIndex < size) {
            swap(start, currLowestIndex);
        }
    }

    private void swap(int i, int j) {

        int tmp = orders[i];
        orders[i] = orders[j];
        orders[j] = tmp;

        tmp = objects[i];
        objects[i] = objects[j];
        objects[j] = tmp;
    }
}
