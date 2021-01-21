package org.mattlang.jc.engine.evaluation.evaltables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class OpeningTest {

    @Test
    public void testWhiteBishop() {
        BoardRepresentation board = new Board2();
        board.setFenPosition("position fen 8/8/8/8/8/8/8/2B5 b k - 2 17 ");
        System.out.println(board.toUniCodeStr());
        int score = Opening.openingPatterns(board, Color.WHITE);
        assertThat(score).isEqualTo(-5);
    }


    @Test
    public void testBlackBishop() {
        BoardRepresentation board = new Board2();
        board.setFenPosition("position fen 2b5/8/8/8/8/8/8/8 b k - 2 17 ");
        System.out.println(board.toUniCodeStr());
        int score = Opening.openingPatterns(board, Color.BLACK);
        assertThat(score).isEqualTo(-5);
    }
}