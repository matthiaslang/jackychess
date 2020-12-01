package org.mattlang.jc.engine.compactmovelist;

import java.util.Iterator;

import org.mattlang.jc.engine.MoveCursor;

/**
 * Lightweight Iterator which does not create Objects for each iterated move by using a
 * "cursor" Object for all iterated moves.
 * Therefore it needs some special care in usage not to save/reuse in loops the cursor object.
 */
public class CompactMoveListIterator implements Iterator<MoveCursor> {

    final CompactMoveList moveList;

    int index = -1;

    private MoveCursor curr = new CompactMoveCursor(this);

    public CompactMoveListIterator(CompactMoveList moveList) {
        this.moveList = moveList;
    }

    @Override
    public boolean hasNext() {
        return index < moveList.size() - 1;
    }

    @Override
    public MoveCursor next() {
        index++;
        return curr;
    }
}
