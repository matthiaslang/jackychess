package org.mattlang.jc.moves;

public final class IntList {

    private int[] data;

    private int size = 0;

    public IntList() {
        this(40);
    }

    public IntList(int maxSize) {
        data = new int[maxSize];
    }

    public final void add(int move) {
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

    public final int size() {
        return size;
    }

    public final int get(int i) {
        return data[i];
    }

    public final void remove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(data, index + 1, data, index,
                    numMoved);
        size--;
    }

    int[] getRaw() {
        return data;
    }

    public void reset() {
        size = 0;
    }
}
