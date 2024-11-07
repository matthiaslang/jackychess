package org.mattlang.jc.moves;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MovePicker;

public class TestTools {

    public static List<Tuple> getAllMoves(RegularMoveIterationPreparer preparer) {
        return getAllMoves(preparer.iterateMoves());
    }

    public static List<Tuple> getAllMoves(StagedMoveIterationPreparer preparer) {
        return getAllMoves(preparer.iterateMoves());
    }

    public static List<Tuple> getAllMoves(MoveBoardIterator iterator) {
        List<Tuple> moves = new ArrayList<>();
        try (iterator) {
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

    public static MoveBoardIterator iterateMoves(MoveList moveList, BoardRepresentation board) {
        MovePicker movePicker = new MovePicker();
        movePicker.init(moveList, 0);
        MoveBoardIterator moveBoardIterator = new MoveBoardIterator();
        moveBoardIterator.init(movePicker, board);
        return moveBoardIterator;
    }
}
