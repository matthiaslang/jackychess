package org.mattlang.jc.board.bitboard;

import org.junit.Test;
import org.mattlang.jc.StopWatch;

public class BitChessBoardTest {

    private static long[] bbFieldMasks = new long[64];

    static {
        for (int i = 0; i < 64; i++) {
            bbFieldMasks[i] = 1L << i;
        }
    }

    /**
     * compares if predefined field masks are faster than doing 1<<pos all the time...
     *
     * But seems to be onl a minimal faster for one billion times....
     */
    @Test
    public void speedComparisonFieldMasks() {
        StopWatch watch = new StopWatch();

        long result = 0;
        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result |= (1L << (i % 64));
        }
        watch.stop();
        System.out.println(watch.toString());
        //        System.out.println(Integer.toString(result));

        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result |= bbFieldMasks[i % 64];
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Long.toString(result));
    }

    @Test
    public void testToStr() {
        System.out.println(BitChessBoard.toStr(1L));
        System.out.println(BitChessBoard.toStrBoard(1L));
    }

}