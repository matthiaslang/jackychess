package org.mattlang.jc.engine.sorting;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SorterTest {

    @Test
    public void alreadySorted(){
        
        Long objects[] = new Long[]{new Long(1), new Long(2), new Long(3)};

        int order[] = new int[]{ 1, 2, 3};

        Sorter<Long> sorter = new Sorter<>(objects, order);

        ArrayList<Long> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Long(1), new Long(2), new Long(3));

    }


    @Test
    public void unsorted(){

        Long objects[] = new Long[]{ new Long(3), new Long(2), new Long(1)};

        int order[] = new int[]{ 3, 2, 1};

        Sorter<Long> sorter = new Sorter<>(objects, order);

        ArrayList<Long> sortedList = new ArrayList<>();
        while(sorter.hasNext()){
            sortedList.add(sorter.next());
        }

        Assertions.assertThat(sortedList).containsExactly(new Long(1), new Long(2), new Long(3));

    }
}