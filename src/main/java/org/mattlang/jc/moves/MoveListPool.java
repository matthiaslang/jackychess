package org.mattlang.jc.moves;

import java.util.ArrayDeque;
import java.util.Queue;

public class MoveListPool {

    private Queue<MoveListImpl> queue = new ArrayDeque<>(200);

    private MoveListPool() {
    }

    public static MoveListPool instance = new MoveListPool();

    public MoveListImpl newOne() {
        MoveListImpl moveList = queue.poll();
        if (moveList == null) {
            return new MoveListImpl();
        } else {
            return moveList;
        }
    }

    public void dispose(MoveListImpl moveList) {
        moveList.reset();
        queue.offer(moveList);
    }

}
