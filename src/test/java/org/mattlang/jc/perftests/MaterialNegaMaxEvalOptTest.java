package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.Benchmarks;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt2;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;
import org.mattlang.jc.uci.UCI;

/**
 * Tests, compares the opt material eval function with the regular one.
 */
public class MaterialNegaMaxEvalOptTest {


    private boolean debug = false;


    @Test
    public void cmpSpeed() {
        BoardRepresentation board = new Board2();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");

        StopWatch watchOpt=Benchmarks.benchmark("Opt Eval",
                () ->{
                    MaterialNegaMaxEvalOpt2 evalOpt = new MaterialNegaMaxEvalOpt2();
                    perftReset();
                    perft(new MoveGeneratorImpl2(), board, WHITE, 4, b -> {
                        int e = evalOpt.eval(b, WHITE);
                    });
                });

//        assertThat(watchOpt.getDuration()).isLessThan(watchNormal.getDuration());
    }


    @Test
    public void testIterativeDeepening() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                .config(c -> c.maxDepth.setValue(6))
                .config(c -> c.timeout.setValue(200000)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }


    @Test
    public void testIterativeDeepeningWithEvalOpt() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
                .config(c -> c.maxDepth.setValue(6))
                .config(c -> c.timeout.setValue(200000))
        .evaluateFunction.set(() -> new MaterialNegaMaxEvalOpt2()));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

}
