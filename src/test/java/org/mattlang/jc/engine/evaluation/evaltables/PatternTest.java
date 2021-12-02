package org.mattlang.jc.engine.evaluation.evaltables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.PieceList;

public class PatternTest {

    @Test
    public void patternTest() {
        Pattern test = new Pattern(new int[] {
                5, 0, 0, 0, 0, 0, 0, 8,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                3, 0, 50, 115, 100, 0, 0, 9,
        });

        Board3 board = new Board3();
        board.setStartPosition();

        int rslt = test.calcScore(board.getWhitePieces().getBishops(), board.getBlackPieces().getBishops(), 1);
        assertThat(rslt).isEqualTo(0);

        PieceList.Array empty = new PieceList.Array(1);
        rslt = test.calcScore(board.getWhitePieces().getBishops(), empty, 1);
        assertThat(rslt).isEqualTo(50);

        rslt = test.calcScore(empty, board.getBlackPieces().getBishops(), 1);
        assertThat(rslt).isEqualTo(-50);
        
        // king test:
        rslt = test.calcScore(board.getWhitePieces().getKing(), board.getBlackPieces().getKing(), 1);
        assertThat(rslt).isEqualTo(0);

        // queen test:
        rslt = test.calcScore(board.getWhitePieces().getQueens(), board.getBlackPieces().getQueens(), 1);
        assertThat(rslt).isEqualTo(0);

        rslt = test.calcScore(board.getWhitePieces().getQueens(), empty, 1);
        assertThat(rslt).isEqualTo(115);

        rslt = test.calcScore(empty, board.getBlackPieces().getQueens(), 1);
        assertThat(rslt).isEqualTo(-115);

        Pattern loadedPattern = Pattern.load("testpattern.csv");
        for (int i = 0; i < 64; i++) {
            assertThat(loadedPattern.getVal(i, WHITE)).isEqualTo(test.getVal(i, WHITE));
            assertThat(loadedPattern.getVal(i, BLACK)).isEqualTo(test.getVal(i, BLACK));
        }

    }
}