package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MoveIteratorImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mattlang.jc.moves.MoveListToStringConverter.mapDebugOrderStr;

public class TestTools {

    public static List<Tuple> getAllMoves(RegularMoveIterationPreparer preparer) {
        List<Tuple> moves = getAllMoves(preparer.iterateMoves());
        Collections.sort(moves);
        return moves;
    }

    public static List<Tuple> getAllMoves(StagedMoveIterationPreparer preparer) {
        return getAllMoves(preparer.iterateMoves());
    }

    public static List<Tuple> getAllMoves(MoveBoardIterator iterator) {
        List<Tuple> moves = new ArrayList<>();
        try (iterator) {
            while (iterator.doNextValidMove()) {
                moves.add(new Tuple(iterator.toStr(), mapDebugOrderStr(iterator.getOrder()), iterator.getOrder()));
            }
        }
        return moves;
    }

    public static MoveBoardIterator iterateMoves(MoveList moveList, BoardRepresentation board) {
        MoveIteratorImpl movePicker = new MoveIteratorImpl();
        movePicker.init(moveList, 0);
        MoveBoardIterator moveBoardIterator = new MoveBoardIterator();
        moveBoardIterator.init(movePicker, board);
        return moveBoardIterator;
    }
}
