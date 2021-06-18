package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SorterTest {

    @Test
    public void alreadySorted(){
        
        long objects[] = new long[]{1, 2, 3};

        int order[] = new int[]{ 1, 2, 3};

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Long> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Long(1), new Long(2), new Long(3));

    }


    @Test
    public void unsorted(){

        long objects[] = new long[]{3,2,1};

        int order[] = new int[]{ 3, 2, 1};

        LongSorter sorter = new LongSorter(objects, objects.length, order);

        ArrayList<Long> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Long(1), new Long(2), new Long(3));

    }
}