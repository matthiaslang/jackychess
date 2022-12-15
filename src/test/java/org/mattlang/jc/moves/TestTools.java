package org.mattlang.jc.moves;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.engine.sorting.MovePicker;

public class TestTools {

    public static List<Tuple> getAllMoves(MoveIterationPreparer preparer) {
        List<Tuple> moves = new ArrayList<>();
        try (MoveBoardIterator iterator = preparer.iterateMoves()) {
            while (iterator.doNextValidMove()) {
                moves.add(new Tuple(iterator.toStr(), iterator.getMoveInt(), iterator.getOrder()));
            }
        }
        return moves;
    }

    public static List<Tuple> getAllMoves(MovePicker picker) {
        List<Tuple> moves = new ArrayList<>();
        while (picker.hasNext()) {
            moves.add(new Tuple("", picker.next(), 0));
        }
        return moves;
    }
}
