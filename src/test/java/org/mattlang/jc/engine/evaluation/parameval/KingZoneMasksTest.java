package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.junit.Test;
import org.mattlang.jc.StopWatch;

public class KingZoneMasksTest {

    @Test
    public void testIdenticalPreCalc() {
        for (int i = 0; i < 64; i++) {
            assertThat(KingZoneMasks.createKingZoneMask(1L << (i), WHITE)).isEqualTo(
                    KingZoneMasks.getKingZoneMask(nWhite, i));
            assertThat(KingZoneMasks.createKingZoneMask(1L << (i), BLACK)).isEqualTo(
                    KingZoneMasks.getKingZoneMask(nBlack, i));
        }
    }

    @Test
    public void speedComparison() {
        StopWatch watch = new StopWatch();

        long result = 0;
        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result |= KingZoneMasks.createKingZoneMask(1L << (i % 64), WHITE);
        }
        watch.stop();
        System.out.println(watch.toString());
        //        System.out.println(Integer.toString(result));

        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result |= KingZoneMasks.getKingZoneMask(nWhite, i % 64);
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Long.toString(result));
    }
}