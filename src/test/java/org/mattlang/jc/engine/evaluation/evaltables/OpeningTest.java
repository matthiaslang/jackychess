package org.mattlang.jc.engine.evaluation.evaltables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class OpeningTest {

    @Test
    public void testWhiteBishop() {
        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/8/8/8/2B5/8/8 b k - 2 17 ");
        System.out.println(board.toUniCodeStr());
        int score = Opening.openingPatterns(board, Color.WHITE);
        // 10 + 50 (virtually two kings on pos 0)
        assertThat(score).isEqualTo(10 + 50);
    }


    @Test
    public void testBlackBishop() {
        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/2b5/8/8/8/8/8 b k - 2 17 ");
        System.out.println(board.toUniCodeStr());
        int score = Opening.openingPatterns(board, Color.BLACK);
        // 10 - 50 (virtually two kings on pos 0)
        assertThat(score).isEqualTo(10 - 50);
    }
}