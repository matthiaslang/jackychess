package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.MoveListImpls;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
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
        NegaMaxAlphaBetaPVS searchMethod = new NegaMaxAlphaBetaPVS(new DefaultEvaluateFunction());

        BoardRepresentation board = new Board3();
        board.setStartPosition();
        board.switchSiteToMove();
        Move move = searchMethod.search(new GameState(board, new SimpleRepetitionChecker()), new GameContext(), 6);
        System.out.println(board.toUniCodeStr());

        System.out.println(move.toStr());
        // with the evaluation function it should yield e7e5:
        assertThat(move.toStr()).isEqualTo("e7e5");
    }

    @Test
    public void testIterativeDeepening() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
        .config(c->c.timeout.setValue(60000))
        .config(c->c.maxDepth.setValue(9)));
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
    public void testProfilingParamEvaluation() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createBitboard()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                        .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .config(c->c.timeout.setValue(18000000))
                .config(c->c.maxDepth.setValue(9))
                .config(c->c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
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
                .config(c->c.maxDepth.setValue(8)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        engine.getBoard().switchSiteToMove();
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    public void testIterativeDeepeningPVSBitboard() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createBitboard()
                .config(c->c.timeout.setValue(600000))
                .config(c->c.maxDepth.setValue(8)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    /**
     * see http://talkchess.com/forum3/viewtopic.php?f=7&t=78474&sid=05defae5cb855cf36d64c12f445ff2fbTt%20fen%2070%20Position%20test
     * Fine 70 test position to analyze tt caching
     * @throws IOException
     */
    @Test
    public void testFine70TTCaching() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createBitboard()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .config(c->c.timeout.setValue(18000000))
                .config(c->c.maxDepth.setValue(14))
//                .config(c->c.aspiration.setValue(false))
                .config(c->c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition("position fen 8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - - 0 1");

        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(state, new GameContext());

        System.out.println(move.toStr());
        
        assertThat(move.toStr()).isEqualTo("a1b1");
    }

    @Test
    public void testPromotionBestMovePieceLetter() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createIterativeDeepeningPVS()
                .config(c -> c.timeout.setValue(60000))
                .config(c -> c.maxDepth.setValue(7)));
        // now starting engine:
        Engine engine = new Engine();

        GameState gameState = engine.getBoard().setFenPosition("position fen 7k/P7/8/8/8/8/K7/8 w k - 2 17 ");

        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(gameState, new GameContext());

        System.out.println(move.toStr());

        // check that the promotion letter is correctly returned in the bestmove:
        assertThat(move.toStr()).isEqualTo("a7a8q");
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

        NegaMaxAlphaBetaPVS negaMax = new NegaMaxAlphaBetaPVS(eval);
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

        BoardRepresentation board = new Board3();
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