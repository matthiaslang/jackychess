package org.mattlang.jc.moves;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;

public class MoveBoardIteratorTest {

    @Test
    public void testiterating() {
        MoveList moves = new MoveListImpl();
        BoardRepresentation board = null;
        CheckChecker checkchecker = null;
        try (MoveBoardIterator mbi = new MoveBoardIterator(moves.iterate(), board, checkchecker)) {
            while (mbi.doNextMove()) {



                // do something
            }
        }
    }

}