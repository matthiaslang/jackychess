package org.mattlang.jc.perftests;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;
import org.mattlang.jc.uci.UCI;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

/**
 * Tests, compares the opt material eval function with the regular one.
 */
public class MaterialNegaMaxEvalOptTest {


    private boolean debug = false;


    @Test
    public void tstOptMatEvalFunc() {
        BoardRepresentation board = new Board2();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");

        MaterialNegaMaxEval eval = new MaterialNegaMaxEval();
        MaterialNegaMaxEvalOpt evalOpt = new MaterialNegaMaxEvalOpt();

        final int[] diffs = {0};

        perftReset();
        perft(new MoveGeneratorImpl2(), board, WHITE, 3, b -> {
            int e = eval.eval(b, WHITE);
            int eOpt = evalOpt.eval(b, WHITE);
            if (e != eOpt) {
                diffs[0]++;
            }
        });

        Assertions.assertThat(diffs[0]).isZero();
    }

    @Test
    public void cmpSpeed() {
        BoardRepresentation board = new Board2();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");

        MaterialNegaMaxEval eval = new MaterialNegaMaxEval();
        MaterialNegaMaxEvalOpt evalOpt = new MaterialNegaMaxEvalOpt();


        StopWatch watch = new StopWatch();
        watch.start();
        perftReset();
        perft(new MoveGeneratorImpl2(), board, WHITE, 3, b -> {
            int e = eval.eval(b, WHITE);
        });
        watch.stop();
        long durationEval = watch.getDuration();
        System.out.println("duration eval: " + durationEval);

        watch = new StopWatch();
        watch.start();
        perftReset();
        perft(new MoveGeneratorImpl2(), board, WHITE, 3, b -> {
            int e = evalOpt.eval(b, WHITE);
        });
        watch.stop();
        long durationEvalOpt = watch.getDuration();
        System.out.println("duration eval opt: " + durationEvalOpt);

        Assertions.assertThat(durationEvalOpt).isLessThan(durationEval);
    }


    @Test
    public void testIterativeDeepening() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningAlphaBeta()
        .setMaxDepth(6)
        .setTimeout(200000));
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
        .setTimeout(200000)
        .setMaxDepth(6)
        .evaluateFunction.set(() -> new MaterialNegaMaxEvalOpt()));
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
