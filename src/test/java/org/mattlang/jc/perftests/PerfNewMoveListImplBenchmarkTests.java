package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.assertPerft;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl3;
import org.mattlang.jc.moves.MoveListPool;
import org.mattlang.jc.uci.UCI;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfNewMoveListImplBenchmarkTests {

    private boolean debug = false;

    @Test
    public void benchmark() throws IOException {
        initLogging();
        UCI.instance.attachStreams();

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        Factory.getDefaults().moveList.set(() -> MoveListPool.instance.newOne());

        StopWatch measureNew = Benchmarks.benchmark(
                "new move list impl",
                () -> {
                    runPosition3();
                });

        System.out.println("new move list impl: " + measureNew.toString());

    }

    public void runPosition3() {
        Board3 board = new Board3();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 14, 1, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 191, 14, 0, 0, 0);

        assertPerft(generator, board, WHITE, 3, 2812, 209, 2, 0, 0);

        assertPerft(generator, board, WHITE, 4, 43238, 3348, 123, 0, 0);

        assertPerft(generator, board, WHITE, 5, 674624, 52051, 1165, 0, 0);

        assertPerft(generator, board, WHITE, 6, 11030083, 940350, 33325, 0, 7552);
    }

}
