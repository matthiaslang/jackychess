package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.engine.MoveGenerator;

public class BoardTest {

    @Test
    public void testStartPosition() {
        Board board = new Board();
        board.setStartPosition();
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testStartPositionWithMoves() {
        Board board = new Board();
        board.setStartPosition();

        board.move(new Move("e2e4"));
        board.move(new Move("e7e5"));
        board.move(new Move("g1f3"));
        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testUndoingMoves() {
        Board board = new Board();
        board.setStartPosition();

        System.out.println("doing moves...");
        UndoMove m1 = board.move(new Move("e2e4"));
        UndoMove m2 = board.move(new Move("e7e5"));
        UndoMove m3 = board.move(new Move("g1f3"));
        System.out.println(board.toUniCodeStr());

        System.out.println("undoing moves...");
        board.move(m3);
        board.move(m2);
        board.move(m1);

        System.out.println(board.toUniCodeStr());

        Board cmpboard = new Board();
        cmpboard.setStartPosition();
        Assertions.assertThat(board.toUniCodeStr()).isEqualTo(cmpboard.toUniCodeStr());
    }


    @Test
    public void testMoveGenFromStartWhite() {
        Board board = new Board();
        board.setStartPosition();

        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> moves = moveGenerator.generate(board, WHITE);

        assertThat(moves)
                .flatExtracting((Function<? super Move, ?>) m -> m.toStr())
                .containsExactlyInAnyOrder(
                        "b1a3",
                        "b1c3",
                        "g1f3",
                        "g1h3",
                        "a2a3",
                        "a2a4",
                        "b2b3",
                        "b2b4",
                        "c2c3",
                        "c2c4",
                        "d2d3",
                        "d2d4",
                        "e2e3",
                        "e2e4",
                        "f2f3",
                        "f2f4",
                        "g2g3",
                        "g2g4",
                        "h2h3",
                        "h2h4"
                );

    }

}