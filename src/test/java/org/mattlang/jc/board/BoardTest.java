package org.mattlang.jc.board;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BoardTest {

    @Test
    public void testStartPosition() {
        BoardRepresentation board = new Board2();
        board.setStartPosition();
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        BoardRepresentation board = new Board2();
        board.setStartPosition();

        board.move(new BasicMove("e2e4"));
        board.move(new BasicMove("e7e5"));
        board.move(new BasicMove("g1f3"));
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testUndoingMoves() {
        BoardRepresentation board = new Board2();
        board.setStartPosition();

        System.out.println("doing moves...");
        Move m1 = board.move(new BasicMove("e2e4"));
        Move m2 = board.move(new BasicMove("e7e5"));
        Move m3 = board.move(new BasicMove("g1f3"));
        System.out.println(board.toUniCodeStr());

        System.out.println("undoing moves...");
        board.move(m3);
        board.move(m2);
        board.move(m1);

        System.out.println(board.toUniCodeStr());

        BoardRepresentation cmpboard = new Board2();
        cmpboard.setStartPosition();
        Assertions.assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }



}