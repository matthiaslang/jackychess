package org.mattlang.jc.moves;

public final class LongList {

    private long[] data;

    private int size = 0;

    public LongList() {
        this(60);
    }

    public LongList(int maxSize) {
        data = new long[maxSize];
    }

    public final void add(long move) {
        if (size < data.length) {
            data[size] = move;
            size++;
        } else {
            int newSize = data.length + data.length;
            long[] newmoves = new long[newSize];
            System.arraycopy(data, 0, newmoves, 0, data.length);
            data = newmoves;

            data[size] = move;
            size++;
        }
    }

    public final int size() {
        return size;
    }

    public final long get(int i) {
        return data[i];
    }

    public final void remove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(data, index + 1, data, index,
                    numMoved);
        size--;
    }

     long[] getRaw() {
        return data;
    }
}
