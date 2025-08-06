package org.mattlang.jc.util;

/**
 * Simple list class for ints.
 */
public final class IntList {

    private int[] data;

    private int size = 0;

    public IntList() {
        this(40);
    }

    public IntList(int maxSize) {
        data = new int[maxSize];
    }

    public void add(int move) {
        if (size < data.length) {
            data[size] = move;
            size++;
        } else {
            int newSize = data.length + data.length;
            int[] newmoves = new int[newSize];
            System.arraycopy(data, 0, newmoves, 0, data.length);
            data = newmoves;

            data[size] = move;
            size++;
        }
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
