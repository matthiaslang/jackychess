package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;
import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SorterTest {

    @Test
    public void alreadySorted(){

        int objects[] = new int[]{1, 2, 3};

        int order[] = new int[]{ 1, 2, 3};

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void partSorted() {

        int objects[] = new int[] { 1, 3, 2 };

        int order[] = new int[] { 1, 3, 2 };

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void sortStability() {

        int objects[] = new int[] { 3, 2, 1 };

        int order[] = new int[] { 2, 2, 1 };

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 3, 2);

    }

    @Test
    public void unsorted() {

        int objects[] = new int[] { 3, 2, 1 };

        int order[] = new int[] { 3, 2, 1 };

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(1, 2, 3);

    }

    @Test
    public void sorttest2() {

        int objects[] = new int[] { 99, 12, 27, 2, 1, 33, 182, 14, 13, 8, 3, 9, 7, 75, -1 };

        int order[] = new int[] { 99, 12, 27, 2, 1, 33, 182, 14, 13, 8, 3, 9, 7, 75, -1 };

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while (sorter.hasNext()) {
            sortedList.add(sorter.next());
        }

        Arrays.sort(order);

        Assertions.assertThat(sortedList).containsExactly(-1, 1, 2, 3, 7, 8, 9, 12, 13, 14, 27, 33, 75, 99, 182);

    }
}