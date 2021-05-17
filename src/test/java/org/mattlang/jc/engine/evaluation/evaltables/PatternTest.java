package org.mattlang.jc.engine.evaluation.evaltables;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.PieceList;

public class PatternTest {

    @Test
    public void patternTest() {
        Pattern test = new Pattern(new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 50, 115, 100, 0, 0, 0,
        });

        Board3 board = new Board3();
        board.setStartPosition();

        int rslt = test.calcScore(board.getWhitePieces().getBishops(), board.getBlackPieces().getBishops(), 1);
        Assertions.assertThat(rslt).isEqualTo(0);

        PieceList.Array empty = new PieceList.Array(1);
        rslt = test.calcScore(board.getWhitePieces().getBishops(), empty, 1);
        Assertions.assertThat(rslt).isEqualTo(50);

        rslt = test.calcScore(empty, board.getBlackPieces().getBishops(), 1);
        Assertions.assertThat(rslt).isEqualTo(-50);
        
        // king test:
        rslt = test.calcScore(board.getWhitePieces().getKing(), board.getBlackPieces().getKing(), 1);
        Assertions.assertThat(rslt).isEqualTo(0);


        // queen test:
        rslt = test.calcScore(board.getWhitePieces().getQueens(), board.getBlackPieces().getQueens(), 1);
        Assertions.assertThat(rslt).isEqualTo(0);

        rslt = test.calcScore(board.getWhitePieces().getQueens(), empty, 1);
        Assertions.assertThat(rslt).isEqualTo(115);

        rslt = test.calcScore(empty, board.getBlackPieces().getQueens(), 1);
        Assertions.assertThat(rslt).isEqualTo(-115);
    }
}