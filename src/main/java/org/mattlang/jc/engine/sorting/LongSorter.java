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

    private int swapCounter = 0;
    private boolean alreadyFullySorted = false;

    public LongSorter(int[] objects, int size, int[] orders) {
        init(objects, size, orders);
    }

    public void init(int[] objects, int size, int[] orders) {
        this.objects = objects;
        this.orders = orders;
        this.size = size;
        start = 0;
        swapCounter = 0;
        alreadyFullySorted = false;
    }

    public static void sort(int[] data, int size, int[] order) {
        new LongSorter(data, size, order).sortData();
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
        if (start >= size - 1) {
            return;
        }
        swapCounter = 0;
        int currLowest = -1;
        int currLowestIndex = -1;
        for (int i = start; i < size - 1; i++) {
            if (orders[i] > orders[i + 1]) {
                swap(i, i + 1);

            }
            if (orders[i] < currLowest || currLowest == -1) {
                currLowest = orders[i];
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
        int tmp = orders[i];
        orders[i] = orders[j];
        orders[j] = tmp;

        int ttmp = objects[i];
        objects[i] = objects[j];
        objects[j] = ttmp;
    }
}
