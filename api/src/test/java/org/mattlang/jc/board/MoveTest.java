package org.mattlang.jc.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.moves.MoveImpl;



public class MoveTest  {

    @Test
    public void testMove() {
        Move move = new MoveImpl("e2e4");
        Assertions.assertEquals("e2e4", move.toStr());
    }
}