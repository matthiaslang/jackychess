package org.mattlang.jc.engine.sorting;

import java.util.Comparator;

import org.mattlang.jc.board.BasicMove;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public class SimpleMoveComparator implements Comparator<Move> {

    private final OrderCalculator orderCalculator;

    public SimpleMoveComparator(final OrderHints orderHints, Color color,
            final int depth, int targetDepth) {
        orderCalculator = new OrderCalculator(orderHints, color, depth, targetDepth);

    }

    @Override
    public int compare(Move o1, Move o2) {
        if (o1.getOrder() == BasicMove.NOT_SORTED) {
            o1.setOrder(orderCalculator.calcOrder(o1));
        }
        if (o2.getOrder() == BasicMove.NOT_SORTED) {
            o2.setOrder(orderCalculator.calcOrder(o2));
        }

        int cmp = o1.getOrder() - o2.getOrder();
        return cmp;
    }

}
