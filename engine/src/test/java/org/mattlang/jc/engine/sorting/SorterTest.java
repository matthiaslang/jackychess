package org.mattlang.jc.engine.sorting;

import java.util.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.engine.MoveList;

public class SorterTest {

    @Test
    public void alreadySorted() {


        MovePicker sorter = new MovePicker();
        sorter.addMoveWithOrder(1, 3);
        sorter.addMoveWithOrder(2, 2);
        sorter.addMoveWithOrder(3, 1);


        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void partSorted() {

        MovePicker sorter = new MovePicker();
        sorter.addMoveWithOrder(1, 3);
        sorter.addMoveWithOrder(3, 1);
        sorter.addMoveWithOrder(2, 2);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    /**
     * the picker sorting is not stable: for identical order values it could deliver them in different order
     * depending on the initial odering.
     *
     * In theory this should not matter at all. If the order of identical values should really depend on their
     * input order, we must simply add a index value to all order values...
     */
    @Test
    @Disabled
    public void sortStability() {

        MovePicker sorter = new MovePicker();
        sorter.addMoveWithOrder(3, 2);
        sorter.addMoveWithOrder(2, 2);
        sorter.addMoveWithOrder(1, 3);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 3, 2);

    }

    @Test
    public void unsorted() {

        MovePicker sorter = new MovePicker();
        sorter.addMoveWithOrder(3, 1);
        sorter.addMoveWithOrder(2, 2);
        sorter.addMoveWithOrder(1, 3);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void sorttest2() {


        MovePicker picker = new MovePicker();
        picker.addMoveWithOrder(99, -99);
        picker.addMoveWithOrder(12, -12);
        picker.addMoveWithOrder(27, -27);
        picker.addMoveWithOrder(2, -2);
        picker.addMoveWithOrder(1, -1);
        picker.addMoveWithOrder(33, -33);
        picker.addMoveWithOrder(182, -182);
        picker.addMoveWithOrder(14, -14);
        picker.addMoveWithOrder(13, -13);
        picker.addMoveWithOrder(8, -8);
        picker.addMoveWithOrder(3, -3);
        picker.addMoveWithOrder(9, -9);
        picker.addMoveWithOrder(7, -7);
        picker.addMoveWithOrder(75, -75);
        picker.addMoveWithOrder(-1, 1);


        ArrayList<Integer> sortedList = new ArrayList<>();
        while (picker.hasNext()) {
            sortedList.add(picker.next());
        }
        Assertions.assertThat(sortedList).containsExactly(-1, 1, 2, 3, 7, 8, 9, 12, 13, 14, 27, 33, 75, 99, 182);

    }

    @Test
    public void sorttest3() {

        MovePicker picker = new MovePicker();
        List<Integer> nums=new ArrayList<>();
        for (int i = 0; i < 250; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums, new Random(57L));

        for (int i = 0; i < 250; i++) {
            picker.addMoveWithOrder(nums.get(i), nums.get(i));
        }


        ArrayList<Integer> sortedList = new ArrayList<>();
        while (picker.hasNext()) {
            sortedList.add(picker.next());
        }

        Collections.sort(nums, Comparator.reverseOrder());
        Assertions.assertThat(sortedList).containsExactly(nums.toArray(new Integer[0]));
    }
}