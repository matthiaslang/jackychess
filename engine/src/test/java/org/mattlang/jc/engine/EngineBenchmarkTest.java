package org.mattlang.jc.engine;

import static org.mattlang.jc.SearchParameter.params;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class EngineBenchmarkTest {

    @Test
    public void benchmark() throws IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();

        StopWatch watchNormal = Benchmarks.benchmark("Normal iterat deep",
                () -> {
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());

                    engine.getBoard().setStartPosition();

                    SearchParameter parameter = params(600000, 7);
                    Move move = engine.go(parameter);

                    System.out.println(move.toStr());

                });

        StopWatch watchOpt = Benchmarks.benchmark("optimized",
                () -> {
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());

                    engine.getBoard().setStartPosition();
                    SearchParameter parameter = params(600000, 7);
                    Move move = engine.go(parameter);

                    System.out.println(move.toStr());

                });

        System.out.println("normal: " + watchNormal.toString());
        System.out.println("optimized: " + watchOpt.toString());

    }

    @Test
    public void benchmarkMiddleGame() throws IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();

        StopWatch watchNormal = Benchmarks.benchmark("Normal iterat deep",
                () -> {
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());
                    engine.getBoard()
                            .setFenPosition(
                                    "position fen r3k1nr/pp3ppp/n1p3q1/3p4/3Pp3/2N3P1/PPPPQP1P/R1B1K2R b KQkq - 0 14");
                    SearchParameter parameter = params(600000, 7);
                    Move move = engine.go(parameter);

                    System.out.println(move.toStr());

                });

        StopWatch watchOpt = Benchmarks.benchmark("optimized",
                () -> {
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());
                    engine.getBoard()
                            .setFenPosition(
                                    "position fen r3k1nr/pp3ppp/n1p3q1/3p4/3Pp3/2N3P1/PPPPQP1P/R1B1K2R b KQkq - 0 14");
                    SearchParameter parameter = params(600000, 7);
                    Move move = engine.go(parameter);

                    System.out.println(move.toStr());

                });

        System.out.println("normal: " + watchNormal.toString());
        System.out.println("optimized: " + watchOpt.toString());

    }

}