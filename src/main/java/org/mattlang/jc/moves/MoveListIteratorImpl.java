package org.mattlang.jc.moves;

import java.util.Iterator;

import org.mattlang.jc.engine.MoveCursor;

public final class MoveListIteratorImpl implements Iterator<MoveCursor> {

    private MoveCursorImpl curr;

    public MoveListIteratorImpl(MoveListImpl movelist) {
        curr = new MoveCursorImpl(movelist);
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
