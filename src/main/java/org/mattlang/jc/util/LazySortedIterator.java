package org.mattlang.jc.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator, which iterates over a sorted list and which sorts lazy via insertion sort by each iteration.
 * This is beneficial if only a small part of the full list needs to be iterated.
 * At the moment, it seems not to help optimizing move ordering; probably because the scores are too bad anyway
 * and too much of the move list needs to be iterated in Alpha/Beta Pruning.
 *
 * @param <T>
 */
public final class LazySortedIterator<T> implements Iterator<T> {

    private final List<T> list;
    private final Comparator<T> comparator;

    private int size;
    private int curr;

    public LazySortedIterator(List<T> list, Comparator<T> comparator) {
        this.list = list;
        this.comparator = comparator;
        size = list.size();
        curr = -1;
    }

    @Override
    public boolean hasNext() {
        return curr < size - 1;
    }

    @Override
    public T next() {
        curr++;
        return insertSort(curr);
    }

    private T insertSort(int curr) {
        if (curr == size - 1) {
            return list.get(curr);
        }
        T currLowest = list.get(curr);
        int currLowestIndex = curr;
        for (int i = curr + 1; i < size; i++) {
            T currElem = list.get(i);
            if (comparator.compare(currElem, currLowest) < 0) {
                currLowest = currElem;
                currLowestIndex = i;
            }
        }

        if (currLowestIndex != curr) {
            swap(curr, currLowestIndex);
        }
        return currLowest;
    }

    private void swap(int i, int j) {
        T tmp = list.get(j);
        list.set(j, list.get(i));
        list.set(i, tmp);
    }
}
