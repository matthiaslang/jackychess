package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.IterativeDeepeningNegaMaxAlphaBeta;
import org.mattlang.jc.engine.search.NegaMax;
import org.mattlang.jc.engine.search.NegaMaxAlphaBeta;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.uci.UCI;

import junit.framework.TestCase;

public class EngineTest extends TestCase {

    @Test
    public void testNegMaxAlpaBeta() {

        // now starting engine:
        Engine engine = new Engine(new NegaMaxAlphaBeta(new MaterialNegaMaxEval()), 6);
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());
        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    public void testIterativeDeepening() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        
        // now starting engine:
        Engine engine = new Engine(new IterativeDeepeningNegaMaxAlphaBeta(), 6);
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    public void testIterativeDeepeningMtdf() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        // now starting engine:
        Engine engine = new Engine(new IterativeDeepeningMtdf(), 6);
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());
        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    public void testNegMax2() {

        // now starting engine:
        Engine engine = new Engine(new NegaMax(new SimpleNegaMaxEval()), 2);
        FenParser parser = new FenParser();
        parser.setPosition("position startpos moves e2e4 a7a6 f2f4 a6a5 a2a4", engine.getBoard());
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());
    }

    /**
     * "Check" is not defined as "invalid" move position, instead we have a weight function which evals a captured
     * king as a "very bad" value, therefore minimax should always get us out of of a chess situation.
     */
    @Test
    public void testCheckSituation() throws IOException {
        UCI.instance.attachStreams(System.in, System.out);
        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();

        Board board = new Board();
        FenParser parser = new FenParser();
        parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        SearchMethod negaMax = new NegaMaxAlphaBeta(eval);
        Move move = negaMax.search(board, 2, Color.BLACK);

        // block with other figure:
        assertThat(move.toStr()).isEqualTo("d8d7");

        System.out.println(move);

    }

    @Test
    public void testCheckSituation2() {

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();

        Board board = new Board();
        FenParser parser = new FenParser();
        parser.setPosition("position fen kp6/1p6/8/6r1/8/Q7/8/4K3 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        NegaMax negaMax = new NegaMax(eval);
        Move move = negaMax.search(board, 2, Color.BLACK);

        System.out.println(board.toUniCodeStr());
        
        // block chess with Rook:
        assertThat(move.toStr()).isEqualTo("g5a5");
        board.move(move);


    }


    @Test
    public void testProblemNoBestMoveFound() {

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();

        Board board = new Board();
        board.setFenPosition("position fen rnb1kbnr/6pp/3Np3/1Pp1P3/5q2/3Q4/PB2BPPP/R4RK1 b kq - 0 16 ");

        System.out.println(board.toUniCodeStr());

        NegaMaxAlphaBeta negaMax = new NegaMaxAlphaBeta(eval);
        Move move = negaMax.search(board, 4, Color.BLACK);

        System.out.println(board.toUniCodeStr());

        // capture knight:
        assertThat(move.toStr()).isEqualTo("f8d6");
        board.move(move);




    }
}