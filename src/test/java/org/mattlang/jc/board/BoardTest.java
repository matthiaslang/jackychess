package org.mattlang.jc.board;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.moves.MoveImpl;

public class BoardTest {

    @Test
    public void testStartPosition() {
        BoardRepresentation board = new Board3();
        board.setStartPosition();
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        BoardRepresentation board = new Board3();
        board.setStartPosition();

        board.move(new MoveImpl("e2e4"));
        board.move(new MoveImpl("e7e5"));
        board.move(new MoveImpl("g1f3"));
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testUndoingMoves() {
        BoardRepresentation board = new Board3();
        board.setStartPosition();

        System.out.println("doing moves...");
        Move m1 = new MoveImpl("e2e4");
        board.move(m1);
        Move m2 = new MoveImpl("e7e5");
        board.move(m2);
        Move m3 = new MoveImpl("g1f3");
        board.move(m3);
        System.out.println(board.toUniCodeStr());

        System.out.println("undoing moves...");
        m3.undo(board);
        m2.undo(board);
        m1.undo(board);

        System.out.println(board.toUniCodeStr());

        BoardRepresentation cmpboard = new Board3();
        cmpboard.setStartPosition();
        Assertions.assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }



}