package org.mattlang.jc.moves;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MovePicker;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;

public class MoveBoardIteratorTest {

    @Test
    public void testiterating() {
        MoveList moves = new MoveList();
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        CheckChecker checkchecker = new BBCheckCheckerImpl();
        MovePicker movePicker = new MovePicker();
        movePicker.init(moves, 0);
        try (MoveBoardIterator mbi = new MoveBoardIterator(movePicker, board, checkchecker)) {
            while (mbi.doNextValidMove()) {

                // do something
            }
        }
    }

}