package org.mattlang.jc.board;

import org.junit.Test;

public class BoardTest {

    @Test
    public void testStartPosition() {
        Board board = new Board();
        board.setStartPosition();
        System.out.println(board.toStr());
    }
}