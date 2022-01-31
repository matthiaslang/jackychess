package org.mattlang.jc.moves;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

import org.mattlang.jc.engine.MoveList;

public class MoveListPool<T extends MoveList> {

    private Queue<T> queue = new ArrayDeque<>(200);

    private Supplier<T> creator;

    public MoveListPool(Supplier<T> creator) {
        this.creator = creator;
    }

    public T newOne() {
        T moveList = queue.poll();
        if (moveList == null) {
            return creator.get();
        } else {
            return moveList;
        }
    }

    public void dispose(T moveList) {
        moveList.reset();
        queue.offer(moveList);
    }

}
