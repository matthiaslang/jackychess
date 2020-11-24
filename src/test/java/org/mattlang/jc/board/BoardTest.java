package org.mattlang.jc.board;

import org.junit.Test;

public class BoardTest {

    @Test
    public void testStartPosition() {
        Board board = new Board();
        board.setStartPosition();
        System.out.println(board.toStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        Board board = new Board();
        board.setStartPosition();
        
//        board.move(new Move("e2e4"));
//        board.move(new Move("e7e5"));
        board.move(new Move("g1f3"));
        System.out.println(board.toStr());
    }
}