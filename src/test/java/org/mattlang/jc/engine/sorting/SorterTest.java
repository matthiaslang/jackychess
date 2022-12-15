package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.moves.MoveListImpl;

public class SorterTest {

    @Test
    public void alreadySorted(){

        MoveListImpl ml = new MoveListImpl();
        ml.addMoveWithOrder(1,1);
        ml.addMoveWithOrder(2,2);
        ml.addMoveWithOrder(3,3);

        MovePicker sorter = new MovePicker(ml,0);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void partSorted() {

        MoveListImpl ml = new MoveListImpl();
        ml.addMoveWithOrder(1,1);
        ml.addMoveWithOrder(3,3);
        ml.addMoveWithOrder(2,2);

        MovePicker sorter = new MovePicker(ml,0);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void sortStability() {
        MoveListImpl ml = new MoveListImpl();
        ml.addMoveWithOrder(3,2);
        ml.addMoveWithOrder(2,2);
        ml.addMoveWithOrder(1,1);

        MovePicker sorter = new MovePicker(ml,0);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 3, 2);

    }

    @Test
    public void unsorted() {

        MoveListImpl ml = new MoveListImpl();
        ml.addMoveWithOrder(3,3);
        ml.addMoveWithOrder(2,2);
        ml.addMoveWithOrder(1,1);

        MovePicker sorter = new MovePicker(ml,0);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void sorttest2() {

        MoveListImpl ml = new MoveListImpl();
        ml.addMoveWithOrder(99,99);
        ml.addMoveWithOrder(12,12);
        ml.addMoveWithOrder(27,27);
        ml.addMoveWithOrder(2,2);
        ml.addMoveWithOrder(1,1);
        ml.addMoveWithOrder(33,33);
        ml.addMoveWithOrder(182,182);
        ml.addMoveWithOrder(14,14);
        ml.addMoveWithOrder(13,13);
        ml.addMoveWithOrder(8,8);
        ml.addMoveWithOrder(3,3);
        ml.addMoveWithOrder(9,9);
        ml.addMoveWithOrder(7,7);
        ml.addMoveWithOrder(75,75);
        ml.addMoveWithOrder(-1,-1);

        MovePicker sorter = new MovePicker(ml,0);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }
        Assertions.assertThat(sortedList).containsExactly(-1, 1, 2, 3, 7, 8, 9, 12, 13, 14, 27, 33, 75, 99, 182);

    }
}