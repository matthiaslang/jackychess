package org.mattlang.jc.moves;

import java.util.Iterator;

import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.LongSorter;

public final class LazySortedMoveListIteratorImpl implements Iterator<MoveCursor> {

    private LazySortedMoveCursorImpl curr;

    public LazySortedMoveListIteratorImpl(LongSorter longSorter) {
        curr = new LazySortedMoveCursorImpl(longSorter);
    }

    @Override
    public boolean hasNext() {
        return curr.hasNext();
    }

    @Override
    public MoveCursor next() {
        curr.next();
        return curr;
    }

    @Override
    public void remove() {
        curr.remove();
    }
}
