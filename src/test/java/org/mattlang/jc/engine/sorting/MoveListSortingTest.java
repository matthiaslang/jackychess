package org.mattlang.jc.engine.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.moves.MoveListImpl;

public class MoveListSortingTest {

    @Test
    public void alreadySorted() {

        MoveListImpl moveList = new MoveListImpl();
        moveList.addMoveWithOrder(1, 1);
        moveList.addMoveWithOrder(2, 2);
        moveList.addMoveWithOrder(3, 3);

        assertThat(getIteratedResult(moveList)).containsExactly(1, 2, 3);

    }

    private List getIteratedResult(MoveListImpl moveList) {
        ArrayList iteratedResult = new ArrayList();
        MoveCursor iterator = moveList.iterate();
        while (iterator.hasNext()) {
            iterator.next();
            iteratedResult.add(iterator.getMoveInt());
        }
        return iteratedResult;
    }

    @Test
    public void partSorted() {

        MoveListImpl moveList = new MoveListImpl();
        moveList.addMoveWithOrder(1, 1);
        moveList.addMoveWithOrder(3, 3);
        moveList.addMoveWithOrder(2, 2);

        assertThat(getIteratedResult(moveList)).containsExactly(1, 2, 3);

    }

    /**
     * The sorting is not stable in the sense as tested here. Sorting changes a given insert order of elements which
     * have the same order value.
     * Question is, if this is relevant, since the order when iterating is in any case fix (if e.g. re-iterated on the
     * same move list again.
     */
    @Test
    public void sortStability() {
        MoveListImpl moveList = new MoveListImpl();
        moveList.addMoveWithOrder(3, 2);
        moveList.addMoveWithOrder(2, 2);
        moveList.addMoveWithOrder(1, 1);

        assertThat(getIteratedResult(moveList)).containsExactly(1, 3, 2);

    }

    @Test
    public void unsorted() {

        MoveListImpl moveList = new MoveListImpl();
        moveList.addMoveWithOrder(3, 3);
        moveList.addMoveWithOrder(2, 2);
        moveList.addMoveWithOrder(1, 1);

        assertThat(getIteratedResult(moveList)).containsExactly(1, 2, 3);

    }

    @Test
    public void sorttest2() {
        MoveListImpl moveList = new MoveListImpl();
        moveList.addMoveWithOrder(99, 99);
        moveList.addMoveWithOrder(12, 12);
        moveList.addMoveWithOrder(27, 27);
        moveList.addMoveWithOrder(2, 2);
        moveList.addMoveWithOrder(1, 1);
        moveList.addMoveWithOrder(33, 33);
        moveList.addMoveWithOrder(182, 182);
        moveList.addMoveWithOrder(14, 14);
        moveList.addMoveWithOrder(13, 13);
        moveList.addMoveWithOrder(8, 8);
        moveList.addMoveWithOrder(3, 3);
        moveList.addMoveWithOrder(9, 9);
        moveList.addMoveWithOrder(7, 7);
        moveList.addMoveWithOrder(75, 75);
        moveList.addMoveWithOrder(-1, -1);

        assertThat(getIteratedResult(moveList)).containsExactly(-1, 1, 2, 3, 7, 8, 9, 12, 13, 14, 27, 33, 75, 99, 182);

    }
}