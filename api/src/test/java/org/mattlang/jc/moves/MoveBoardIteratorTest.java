package org.mattlang.jc.moves;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MoveIteratorImpl;

public class MoveBoardIteratorTest {

    @Test
    public void testiterating() {
        MoveList moves = new MoveList();
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        MoveIteratorImpl movePicker = new MoveIteratorImpl();
        movePicker.init(moves, 0);
        try (MoveBoardIterator mbi = new MoveBoardIterator(movePicker, board)) {
            while (mbi.doNextValidMove()) {

                // do something
            }
        }
    }

}