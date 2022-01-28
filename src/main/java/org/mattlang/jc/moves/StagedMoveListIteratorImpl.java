package org.mattlang.jc.moves;

import java.util.Iterator;

import org.mattlang.jc.engine.MoveCursor;

public class StagedMoveListIteratorImpl implements Iterator<MoveCursor> {

    StagedMoveCursor curr;

    public StagedMoveListIteratorImpl(StagedMoveListImpl movelist) {
        curr = new StagedMoveCursor(movelist);

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

}
