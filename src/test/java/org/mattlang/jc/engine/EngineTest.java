package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.CacheImpls;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.MoveListImpls;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.search.MultiThreadedIterativeDeepeningV2;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class EngineTest {

    @Test
    public void testIterativeDeepening() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .config(c -> c.timeout.setValue(60000))
                .config(c -> c.maxDepth.setValue(9)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        //        assertThat(move.toStr()).isEqualTo("e2e3");
    }

    /**
     * todo analyze: the pv is only properly delivered when ttcache is disabled.
     * with ttcache we are currently not able to reconstruct it via the cache.
     * why?
     * - have we problems with out replacement strategy?
     * - do we have issues with the zobrist keys? with the distribution into the bucket index?
     * - do we need a separate tt cache for pvs?
     * - is our pv array handling wrong?
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void testNoSMP() throws IOException {

        System.setProperty(LOGGING_ACTIVATE, "false");
        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                //                .config(c -> c.cacheImpls.setValue(CacheImpls.V3))
                .config(c -> c.timeout.setValue(36000000))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.maxDepth.setValue(11))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        Move move = engine.go(new GameState(engine.getBoard()), gameContext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    @Ignore
    public void testError() throws IOException {

        System.setProperty(LOGGING_ACTIVATE, "false");
        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                //                .config(c -> c.cacheImpls.setValue(CacheImpls.V3))
                .config(c -> c.timeout.setValue(36000000))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.maxDepth.setValue(11))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard()
                .setFenPosition(
                        "position startpos moves d2d4 d7d6 c2c4 e7e5 d4e5 d6e5 d1d8 e8d8 b1c3 c7c6 f2f4 b8d7 g1f3 g8e7 f4e5 e7g6 e2e4 f8c5 f1e2 d8c7 c3a4 c5b4 e1f2 d7e5 f3e5 g6e5 c4c5 c8e6 a2a3 b4a5 c1f4 f7f6 b2b4 e6b3");
        System.out.println(engine.getBoard().toUniCodeStr());
        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        try {
            Move move = engine.go(gameState, gameContext);
            System.out.println(move.toStr());

            // with the evaluation function it should yield e7e6:
//            assertThat(move.toStr()).isEqualTo("e7e6");
        } catch (SearchException se) {
            System.out.println(se.toStringAllInfos());
            se.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void testLazySMP() throws IOException {

        System.setProperty(LOGGING_ACTIVATE, "false");
        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .searchMethod.set(() -> new MultiThreadedIterativeDeepeningV2())
                .config(c -> c.cacheImpls.setValue(CacheImpls.STANDARD))
                .config(c -> c.hash.setValue(512))
                .config(c -> c.timeout.setValue(36000000))
                .config(c -> c.maxDepth.setValue(11))
                .config(c -> c.maxThreads.setValue(4))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        Move move = engine.go(new GameState(engine.getBoard()), gameContext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

    @Test
    public void testIterativeDeepeningPVS() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(60000))
                .config(c -> c.maxDepth.setValue(8)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        engine.getBoard().switchSiteToMove();
        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        Move move = engine.go(new GameState(engine.getBoard()), gameContext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e5");
    }

    /**
     * see
     * http://talkchess.com/forum3/viewtopic.php?f=7&t=78474&sid=05defae5cb855cf36d64c12f445ff2fbTt%20fen%2070%20Position%20test
     * Fine 70 test position to analyze tt caching
     *
     * https://www.chessprogramming.org/Lasker-Reichhelm_Position
     *
     * the only winning move is a1b1.
     *
     * @throws IOException
     */
    @Test
    public void testFine70TTCaching() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .config(c -> c.timeout.setValue(18000000))
                .config(c -> c.maxDepth.setValue(21))
                //                .config(c->c.aspiration.setValue(false))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition("position fen 8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - - 0 1");

        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(state, new GameContext());

        System.out.println(move.toStr());

        assertThat(move.toStr()).isEqualTo("a1b1");
    }

    @Test
    public void testEndGameWeirdProblem() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .moveList.set(MoveListImpls.OPTIMIZED.createSupplier())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .config(c -> c.timeout.setValue(2000))
                .config(c -> c.maxDepth.setValue(20))
                .config(c -> c.useLateMoveReductions.setValue(true))
                .config(c -> c.deltaCutoff.setValue(true))
                .config(c -> c.razoring.setValue(true))
                .config(c -> c.useNullMoves.setValue(true))
                .config(c -> c.staticNullMove.setValue(true))
                .config(c -> c.futilityPruning.setValue(true))
                .config(c -> c.cacheImpls.setValue(CacheImpls.STANDARD))
                //                .config(c->c.aspiration.setValue(false))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        GameState state = engine.getBoard()
                .setFenPosition("position fen 8/8/8/8/8/6K1/1Q6/3k4 w - - 39 143  ");

        System.out.println(engine.getBoard().toUniCodeStr());
        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());
        Move move = engine.go(state, gameContext);

        System.out.println(move.toStr());

        // now do the same when tt cache is filled from previous search:
        state = engine.getBoard()
                .setFenPosition("position fen 8/8/8/8/8/6K1/1Q6/3k4 w - - 39 143  ");

        System.out.println(engine.getBoard().toUniCodeStr());

        move = engine.go(state, gameContext);
        System.out.println(move.toStr());

    }

    @Test
    public void testPromotionBestMovePieceLetter() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
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
        EvaluateFunction eval = new MinimalPstEvaluation();

        BoardRepresentation board = Factory.getDefaults().boards.create();
        FenParser parser = new FenParser();
        GameState gameState =
                parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        SearchMethod negaMax = new NegaMaxAlphaBetaPVS(eval);
        Move move = negaMax.search(gameState, new GameContext(), 2);

        // block with other figure:
        assertThat(move.toStr()).isEqualTo("d8d7");

        System.out.println(move);

    }

    @Test
    public void testCheckSituation2() {

        EvaluateFunction eval = new MinimalPstEvaluation();

        BoardRepresentation board = Factory.getDefaults().boards.create();
        FenParser parser = new FenParser();
        GameState gameState = parser.setPosition("position fen kp6/1p6/8/6r1/8/Q7/8/4K3 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        NegaMaxAlphaBetaPVS negaMax = new NegaMaxAlphaBetaPVS(eval);
        Move move = negaMax.search(gameState, new GameContext(), 2);

        System.out.println(board.toUniCodeStr());

        // block chess with Rook:
        assertThat(move.toStr()).isEqualTo("g5a5");
        board.domove(move);

    }

    @Test
    public void testProblemNoBestMoveFound() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable());

        EvaluateFunction eval = new MinimalPstEvaluation();

        BoardRepresentation board = new BitBoard();
        GameState gameState =
                board.setFenPosition("position fen rnb1kbnr/6pp/3Np3/1Pp1P3/5q2/3Q4/PB2BPPP/R4RK1 b kq - 0 16 ");

        System.out.println(board.toUniCodeStr());

        NegaMaxAlphaBetaPVS negaMax = new NegaMaxAlphaBetaPVS(eval);
        Move move = negaMax.search(gameState, new GameContext(), 4);

        System.out.println(board.toUniCodeStr());

        // capture knight:
        assertThat(move.toStr()).isEqualTo("f8d6");
        board.domove(move);

    }
}