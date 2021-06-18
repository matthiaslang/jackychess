package org.mattlang.jc.engine.sorting;

/**
 * Sorts objects by a separate orders array.
 * Does this lazy, to safe time when e.g. not all moves are needed when a cut off happened.
 * Kind of lazy merge sort to prevent sorting the complete array.
 */
public class LongSorter {

    private long[] objects;
    private int[] orders;

    private int size = 0;

    private int start = 0;

    private int swapCounter = 0;
    private boolean alreadyFullySorted = false;

    public LongSorter(long[] objects, int size, int[] orders) {
        this.objects = objects;
        this.orders = orders;
        this.size = size;
    }

    public static void sort(long[] data, int size, int[] order) {
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

    public long next() {
        if (!alreadyFullySorted) {
            sortRound();
        }
        size--;
        return objects[start++];
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

        long ttmp = objects[i];
        objects[i] = objects[j];
        objects[j] = ttmp;
    }
}
