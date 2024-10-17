package org.mattlang.jc.engine.evaluation.evaltables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;

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

        BitBoard board = new BitBoard();
        board.setStartPosition();

        BitChessBoard bb = board.getBoard();
        int rslt = test.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack));
        assertThat(rslt).isEqualTo(0);

        rslt = test.calcScore(bb.getBishops(nWhite), 0L);
        assertThat(rslt).isEqualTo(50);

        rslt = test.calcScore(0L, bb.getBishops(nBlack));
        assertThat(rslt).isEqualTo(-50);

        // king test:
        rslt = test.calcScore(bb.getKings(nWhite), bb.getKings(nBlack));
        assertThat(rslt).isEqualTo(0);

        // queen test:
        rslt = test.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack));
        assertThat(rslt).isEqualTo(0);

        rslt = test.calcScore(bb.getQueens(nWhite), 0L);
        assertThat(rslt).isEqualTo(115);

        rslt = test.calcScore(0L, bb.getQueens(nBlack));
        assertThat(rslt).isEqualTo(-115);

        Pattern loadedPattern = Pattern.load("testpattern.csv");
        for (int i = 0; i < 64; i++) {
            assertThat(loadedPattern.getVal(i, WHITE)).isEqualTo(test.getVal(i, WHITE));
            assertThat(loadedPattern.getVal(i, BLACK)).isEqualTo(test.getVal(i, BLACK));
        }

        String patternStr=loadedPattern.toPatternStr();
        System.out.println(patternStr);

        Pattern reParsedPattern = Pattern.parsePattern(new ByteArrayInputStream(patternStr.getBytes()));

        assertThat(reParsedPattern).isEqualTo(loadedPattern);

    }
}