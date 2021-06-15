package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.NegaMax;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class EngineTest {

    @Test
    public void testNegMaxAlpaBeta() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        // now starting engine:
        Engine engine = new Engine(new NegaMaxAlphaBetaPVS(new DefaultEvaluateFunction()), 6);
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
        Factory.setDefaults(Factory.createStable()
        .config(c->c.timeout.setValue(60000))
        .config(c->c.maxDepth.setValue(7)));
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
    public void testIterativeDeepeningPVS() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningPVS()
                .config(c->c.timeout.setValue(60000))
                .config(c->c.maxDepth.setValue(7)));
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
    @Ignore
    public void testIterativeDeepeningMtdf() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningMtdf());
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
    @Ignore
    public void testMtdfProblem() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        // now starting engine:
        Engine engine = new Engine(new IterativeDeepeningMtdf(), 6);
        engine.getBoard().setFenPosition("position startpos moves e2e4 e7e6 g1f3 d8f6 f1c4 f6f3 g2f3");
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        assertThat(move.toStr()).isEqualTo("d7d5");
    }



    /**
     * "Check" is not defined as "invalid" move position, instead we have a weight function which evals a captured
     * king as a "very bad" value, therefore minimax should always get us out of of a chess situation.
     */
    @Test
    public void testCheckSituation() throws IOException {
        UCI.instance.attachStreams(System.in, System.out);
        DefaultEvaluateFunction eval = new DefaultEvaluateFunction();

        BoardRepresentation board = Factory.getDefaults().boards.create();
        FenParser parser = new FenParser();
        GameState gameState =parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        SearchMethod negaMax = new NegaMaxAlphaBetaPVS(eval);
        Move move = negaMax.search(gameState, new GameContext(),2);

        // block with other figure:
        assertThat(move.toStr()).isEqualTo("d8d7");

        System.out.println(move);

    }

    @Test
    public void testCheckSituation2() {

        DefaultEvaluateFunction eval = new DefaultEvaluateFunction();

        BoardRepresentation board = Factory.getDefaults().boards.create();
        FenParser parser = new FenParser();
        GameState gameState = parser.setPosition("position fen kp6/1p6/8/6r1/8/Q7/8/4K3 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        NegaMax negaMax = new NegaMax(eval);
        Move move = negaMax.search(gameState, new GameContext(),2);

        System.out.println(board.toUniCodeStr());
        
        // block chess with Rook:
        assertThat(move.toStr()).isEqualTo("g5a5");
        board.move(move);


    }


    @Test
    public void testProblemNoBestMoveFound() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        
        DefaultEvaluateFunction eval = new DefaultEvaluateFunction();

        BoardRepresentation board = new Board2();
        GameState gameState = board.setFenPosition("position fen rnb1kbnr/6pp/3Np3/1Pp1P3/5q2/3Q4/PB2BPPP/R4RK1 b kq - 0 16 ");

        System.out.println(board.toUniCodeStr());

        NegaMaxAlphaBetaPVS negaMax = new NegaMaxAlphaBetaPVS(eval);
        Move move = negaMax.search(gameState, new GameContext(),4);

        System.out.println(board.toUniCodeStr());

        // capture knight:
        assertThat(move.toStr()).isEqualTo("f8d6");
        board.move(move);




    }
}