package org.mattlang.jc.engine.sorting;

import java.util.Comparator;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.PVList;

public class SimpleMoveComparator implements Comparator<Move> {

    private final Move pvMove;

    public SimpleMoveComparator(final PVList prevPvlist, final int depth, int targetDepth) {
        int index = targetDepth - depth;
        pvMove = prevPvlist != null ? prevPvlist.get(index) : null;
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (o1.getOrder() == 0) {
            o1.setOrder(calcPVCmp(o1));
        }
        if (o2.getOrder() == 0) {
            o2.setOrder(calcPVCmp(o2));
        }

        int cmp = o1.getOrder() - o2.getOrder();
        if (cmp != 0) {
            return cmp;
        }
        return o2.getMvvLva() - o1.getMvvLva();

    }

    private int calcPVCmp(Move m) {
        if (pvMove != null) {
            if (pvMove.equals(m)) {
                return -1000000;
            }
        }
        return 100;
    }


}
