package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.moves.MoveListImpl;
import org.mattlang.jc.moves.MoveListPool;
import org.mattlang.jc.uci.UCI;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class MoveListImplBenchmarkTests {

    private static final int N = 10000000;
    private boolean debug = false;

    @Test
    public void benchmark() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        
        StopWatch measureNormal = Benchmarks.benchmark(
                "normal move list",
                () -> {
                    normal();
                });

        StopWatch measureNew = Benchmarks.benchmark(
                "new move list impl",
                () -> {
                    alternative();
                });

        System.out.println("normal move list: " + measureNormal.toString());
        System.out.println("new move list impl: " + measureNew.toString());
    }

    private void alternative() {
        for (int i = 0; i < N; i++) {
            MoveListImpl b = MoveListPool.instance.newOne();
            for (int j = 0; j < 50; j++) {
                b.genMove(Figure.B_King.figureCode, 2, 10, (byte) 32);
            }
            for (MoveCursor cursor: b){
                cursor.isPawnPromotion();
            }

            MoveListPool.instance.dispose(b);
        }
    }

    private void normal() {
        for (int i = 0; i < N; i++) {
            BasicMoveList b = new BasicMoveList();
            for (int j = 0; j < 50; j++) {
                b.genMove(Figure.B_King.figureCode, 2, 10, (byte) 32);
            }
            for (MoveCursor cursor : b) {
                cursor.isPawnPromotion();
            }
        }
    }

}
