package org.mattlang.jc.moves;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;
import static org.mattlang.jc.moves.TestTools.getAllMoves;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class StagedMoveIterationPreparerTest {

    @Test
    public void testStagingWithHashMove() {

        SearchThreadContext stc = new SearchThreadContext();
        stc.getKillerMoves().addKiller(10048001, 1);
        stc.getKillerMoves().addKiller( 19485185, 1);
        int parentMove = 4711;
        stc.getCounterMoveHeuristic().addCounterMove(Color.WHITE.ordinal(), parentMove, 1589889);

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        int hashMove = 10572289;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, parentMove);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, parentMove);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));
    }

    @Test
    public void testFens() {
        testCmpFen("r5k1/2pq2b1/3p1r1p/3P4/N1PQ4/1P3n2/P2B1PP1/R4RK1 w - - 1 29", Color.WHITE);
        testCmpFen("r5k1/2pq2b1/3p2rp/3P4/N1PQ4/1P3P2/P2B1P2/R4RK1 w - - 1 30", Color.WHITE);
        testCmpFen("r5k1/2pq2b1/3p2rp/3P4/N1P3Q1/1P3P2/P2B1P2/R4RK1 b - - 2 30", Color.BLACK);
        testCmpFen("r5k1/2pq2b1/3p3p/3P4/N1P3r1/1P3P2/P2B1P2/R4RK1 w - - 0 31", Color.WHITE);
    }

    private void testCmpFen(String fen, Color whoOnMove) {
        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen " + fen);
        System.out.println(board.toUniCodeStr());

        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, whoOnMove, 1, 0, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, whoOnMove, 1, 0, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));
    }

    @Test
    public void testStagingWithoutHashMove() {

        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        int hashMove = 0;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));

    }

    @Test
    public void testStagingWithoutCaptures() {

        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 4k3/8/1Q6/4K3/8/8/8/8 w - - 0 0 ");
        System.out.println(board.toUniCodeStr());

        int hashMove = 22188545;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));

    }


    @Test
    public void testStagingWithOnlyBadCaptures() {

        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 3bk3/8/1Q6/4K3/8/8/8/8 w - - 0 0 ");
        System.out.println(board.toUniCodeStr());

        int hashMove = 22188545;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));

    }

    @Test
    public void testStagingWithPromotions() {

        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 1r2k3/P7/1Qp5/4K3/8/8/8/8 w - - 0 0 ");
        System.out.println(board.toUniCodeStr());

        int hashMove =1095930369;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.NORMAL, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m->m.order).collect(Collectors.toList())).isEqualTo(movesRegular.stream().map(m->m.order).collect(
                Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));
    }


    @Test
    public void testStagingWithQuiescence() {

        SearchThreadContext stc = new SearchThreadContext();
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        int hashMove = 10572289;
        RegularMoveIterationPreparer preparer = new RegularMoveIterationPreparer();
        preparer.prepare(stc, GenMode.QUIESCENCE, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> movesRegular = getAllMoves(preparer);
        System.out.println(movesRegular);

        StagedMoveIterationPreparer stagedPreparer = new StagedMoveIterationPreparer();
        stagedPreparer.prepare(stc, GenMode.QUIESCENCE, board, Color.WHITE, 1, hashMove, 0);
        List<Tuple> moves = getAllMoves(stagedPreparer);
        System.out.println(moves);

        // should have same move count
        assertThat(moves).hasSameSizeAs(movesRegular);

        // should have same order of the moves
        assertThat(moves.stream().map(m -> m.order).collect(Collectors.toList())).isEqualTo(
                movesRegular.stream().map(m -> m.order).collect(
                        Collectors.toList()));

        // but due to the fact that the moves are created by different stages, the exact order of moves with same move order is not identical
        assertThat(moves).containsExactlyInAnyOrder(movesRegular.toArray(new Tuple[0]));

    }

    @Test
    public void test() {

        System.out.println(Integer.toBinaryString(128));
        System.out.println(Integer.toBinaryString(128 + 4));
        System.out.println(Integer.toBinaryString(128 - 4));
        System.out.println(Integer.toBinaryString(-128));
        System.out.println(Integer.toBinaryString(-128 + 4));
        System.out.println(Integer.toBinaryString(-128 - 4));
    }

    @Test
    public void testRegular() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(60000))
                .config(c -> c.maxDepth.setValue(20)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen r5k1/2pq2b1/3p1r1p/3P4/N1PQ4/1P3n2/P2B1PP1/R4RK1 w - - 1 29");
        System.out.println(engine.getBoard().toUniCodeStr());

        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        Move move = engine.go(new GameState(engine.getBoard()), gameContext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("g2f3");
    }

    @Test
    public void testStaged() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(60000))
                .config(c -> c.maxDepth.setValue(20)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen r5k1/2pq2b1/3p1r1p/3P4/N1PQ4/1P3n2/P2B1PP1/R4RK1 w - - 1 29");
        System.out.println(engine.getBoard().toUniCodeStr());

        GameContext gameContext = new GameContext(Factory.getDefaults().getConfig());

        Move move = engine.go(new GameState(engine.getBoard()), gameContext);

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("g2f3");
    }
}