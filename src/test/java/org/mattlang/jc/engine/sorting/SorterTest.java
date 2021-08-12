package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SorterTest {

    @Test
    public void alreadySorted(){

        int objects[] = new int[]{1, 2, 3};

        int order[] = new int[]{ 1, 2, 3};

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Integer(1), new Integer(2), new Integer(3));

    }


    @Test
    public void unsorted(){

        int objects[] = new int[]{3,2,1};

        int order[] = new int[]{ 3, 2, 1};

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Integer> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Integer(1), new Integer(2), new Integer(3));

    }
}