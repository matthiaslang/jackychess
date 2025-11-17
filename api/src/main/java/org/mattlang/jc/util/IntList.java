package org.mattlang.jc.util;

/**
 * Simple list class for ints with fixed maximum size.
 */
public final class IntList {

    private final int[] data;

    private int size = 0;

    public IntList(int maxSize) {
        data = new int[maxSize];
    }

    public void add(int move) {
        data[size] = move;
        size++;
    }

    public int size() {
        return size;
    }

    public int get(int i) {
        return data[i];
    }

    public void remove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(data, index + 1, data, index,
                    numMoved);
        size--;
    }

    public void reset() {
        size = 0;
    }

    public void cutToSize(int newSize) {
        if (newSize > size) {
            throw new IllegalArgumentException("can only shrink size, not extend!");
        }
        this.size = newSize;
    }
}
