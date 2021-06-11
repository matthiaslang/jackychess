package org.mattlang.jc.engine.sorting;

/**
 * Sorts objects by a separate orders array.
 * Does this lazy, to safe time when e.g. not all moves are needed when a cut off happened.
 * Kind of lazy merge sort to prevent sorting the complete array.
 */
public class Sorter<T> {

    private T[] objects;
    private int[] orders;

    private int size = 0;

    private int start = 0;

    public Sorter(T[] objects, int[] orders) {
        this.objects = objects;
        this.orders = orders;
        size = objects.length;
    }

    public boolean hasNext() {
        return size > 0;
    }

    public T next() {
        sortRound();
        size--;
        return objects[start++];
    }

    private void sortRound() {
        if (start == objects.length - 1) {
            return;
        }
        int currLowest = -1;
        int currLowestIndex = -1;
        for (int i = start; i < objects.length - 1; i++) {
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
    }

    private void swap(int i, int j) {
        int tmp = orders[i];
        orders[i] = orders[j];
        orders[j] = tmp;

        T ttmp = objects[i];
        objects[i] = objects[j];
        objects[j] = ttmp;
    }
}
