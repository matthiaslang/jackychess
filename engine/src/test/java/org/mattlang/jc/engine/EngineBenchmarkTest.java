package org.mattlang.jc.engine;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class EngineBenchmarkTest {

    @Test
    public void benchmark() throws IOException {
        Logging.initLogging();
        UCI.instance.attachStreams();

        StopWatch watchNormal = Benchmarks.benchmark("Normal iterat deep",
                () -> {
                    Factory.setDefaults(Factory.createStable()
                            .config(c -> {
                                c.timeout.setValue(600000);
                                c.maxDepth.setValue(7);
                            }));

                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());

                    engine.getBoard().setStartPosition();
                    Move move = engine.go();

                    System.out.println(move.toStr());

                });

        StopWatch watchOpt = Benchmarks.benchmark("optimized",
                () -> {
                    Factory.setDefaults(Factory.createStable()
                            .config(c -> c.timeout.setValue(600000))
                            .searchMethod.set(() -> new IterativeDeepeningPVS())
                            //                            .evaluateFunction.set(MaterialNegaMaxEvalOpt::new)
                            //                            .legalMoveGenerator.set(LegalMoveGeneratorImpl4::new)

                            .config(c -> c.maxDepth.setValue(7)));
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());

                    engine.getBoard().setStartPosition();
                    Move move = engine.go();

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
                    Factory.setDefaults(Factory.createStable()
                            .config(c -> {
                                c.timeout.setValue(600000);
                                c.maxDepth.setValue(7);
                            }));
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());
                    engine.getBoard()
                            .setFenPosition(
                                    "position fen r3k1nr/pp3ppp/n1p3q1/3p4/3Pp3/2N3P1/PPPPQP1P/R1B1K2R b KQkq - 0 14");
                    Move move = engine.go();

                    System.out.println(move.toStr());

                });

        StopWatch watchOpt = Benchmarks.benchmark("optimized",
                () -> {
                    Factory.setDefaults(Factory.createStable()
                            .config(c -> c.timeout.setValue(600000))
                            .searchMethod.set(() -> new IterativeDeepeningPVS(new NegaMaxAlphaBetaPVS()))
                            //                            .evaluateFunction.set(MaterialNegaMaxEvalOpt::new)
                            //                            .legalMoveGenerator.set(LegalMoveGeneratorImpl4::new)

                            .config(c -> c.maxDepth.setValue(7)));
                    // now starting engine:
                    Engine engine = new Engine(new BitBoard());
                    engine.getBoard()
                            .setFenPosition(
                                    "position fen r3k1nr/pp3ppp/n1p3q1/3p4/3Pp3/2N3P1/PPPPQP1P/R1B1K2R b KQkq - 0 14");
                    Move move = engine.go();

                    System.out.println(move.toStr());

                });

        System.out.println("normal: " + watchNormal.toString());
        System.out.println("optimized: " + watchOpt.toString());

    }

}