package org.mattlang.jc.moves;

import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.OrderCalculator;

import static org.mattlang.jc.moves.MoveToStringConverter.toLongAlgebraic;

/**
 * Methods to convert Movelists to String representations.
 */
public class MoveListToStringConverter {


    public static String movedescr(MoveCursor move) {
        String moveDescr = toLongAlgebraic(move) + ": " + mapDebugOrderStr(move.getOrder()) + "(" + move.getOrder() + ")";
        return moveDescr;
    }

    public static String mapDebugOrderStr(Integer o) {
        int oi = o.intValue();
        if (oi == OrderCalculator.HASHMOVE_SCORE) {
            return "1.HASH";
        }
        if (OrderCalculator.isGoodCapture(oi)) {
            return "2.GOOD CAP";
        }
        if (OrderCalculator.isGoodPromotion(oi)) {
            return "3.GOOD PROM";
        }
        if (OrderCalculator.isKillerMove(oi)) {
            return "4.KILLER";
        }
        if (OrderCalculator.isCounterMove(oi)) {
            return "5.COUNTER";
        }
        if (OrderCalculator.isHistory(oi)) {
            return "6.HISTORY";
        }
        if (OrderCalculator.isBadCapture(oi)) {
            return "8.BAD CAP";
        }
        return "7.QUIET";
    }
}
