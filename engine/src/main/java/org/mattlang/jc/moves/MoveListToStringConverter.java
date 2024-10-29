package org.mattlang.jc.moves;

import static org.mattlang.jc.moves.MoveToStringConverter.toLongAlgebraic;

import java.util.ArrayList;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;

/**
 * Methods to convert Movelists to String representations.
 */
public class MoveListToStringConverter {

    public static ArrayList<String> toString(MoveList moveList) {
        return toString(moveList, 0, moveList.size());
    }

    public static ArrayList<String> toString(MoveList moveList, int start, int stop) {
        MoveImpl moveimpl = new MoveImpl("a1a1");
        ArrayList<String> moves = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            int move = moveList.get(i);
            int order = moveList.getOrder(i);
            moveimpl.fromLongEncoded(move);
            String moveDescr = movedescr(move, order);
            moves.add(moveDescr);
        }
        return moves;
    }

    public static String movedescr(int move, int order) {
        MoveImpl moveimpl = new MoveImpl("a1a1");
        moveimpl.fromLongEncoded(move);
        return movedescr(moveimpl, order);
    }

    public static String movedescr(MoveCursor moveCursor) {
        return movedescr(moveCursor, moveCursor.getOrder());
    }

    public static String movedescr(Move move, int order) {
        String moveDescr = toLongAlgebraic(move) + ": " + mapDebugOrderStr(order) + "(" + order + ")";
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
