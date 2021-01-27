package org.mattlang.jc.engine;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt;
import org.mattlang.jc.uci.UCI;

public class EngineBenchmarkTest {


    @Test
    public void benchmark() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        
        StopWatch watchNormal= Benchmarks.benchmark("Normal iterat deep",
                () ->{
                    Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                            .setTimeout(600000)
                            .setMaxDepth(7));
                    // now starting engine:
                    Engine engine = new Engine(new Board2());

                    engine.getBoard().setStartPosition();
                    Move move = engine.go();

                    System.out.println(move.toStr());

                });

        StopWatch watchOpt= Benchmarks.benchmark("optimized",
                () ->{
                    Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                            .setTimeout(600000)
                            .evaluateFunction.set(MaterialNegaMaxEvalOpt::new)
//                            .legalMoveGenerator.set(LegalMoveGeneratorImpl4::new)

                            .setMaxDepth(7));
                    // now starting engine:
                    Engine engine = new Engine(new Board2());

                    engine.getBoard().setStartPosition();
                    Move move = engine.go();

                    System.out.println(move.toStr());

                });

        System.out.println("normal: "+ watchNormal.toString());
        System.out.println("optimized: "+ watchOpt.toString());

    }

}