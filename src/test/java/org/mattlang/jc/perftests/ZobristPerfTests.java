package org.mattlang.jc.perftests;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

/**
 * PerfTests for zobrist hashing.
 *
 */
public class ZobristPerfTests {

    @Test
    public void compareSpeed() {

        StopWatch hashMeasure = benchmark(
                "board2 normal hash",
                () -> {
            Board2 board = new Board2();
            board.setStartPosition();
            perftReset();
            perft(new LegalMoveGeneratorImpl3(), board, WHITE, 5, visitedBoard -> {
                int hash = visitedBoard.hashCode();
            });
        });

        StopWatch zobristMeasure = benchmark(
                "board3 zobrist hash",
                () -> {
            Board3 board = new Board3();
            board.setStartPosition();
            perftReset();
            perft(new LegalMoveGeneratorImpl3(), board, WHITE, 5, visitedBoard -> {
                long zobristHash = visitedBoard.getZobristHash();
            });
        });

        System.out.println("zobrist time: " + zobristMeasure.toString());
        System.out.println("hash    time: " + hashMeasure.toString());

        Assertions.assertThat(zobristMeasure.getDuration()).isLessThan(hashMeasure.getDuration());

    }

    @Test
    public void initialPosZobristHashUnique() {
        Board3 board = new Board3();
        board.setStartPosition();

        assertNoCollisions(board, 5);

    }


    @Test
    public void position2() {
        Board3 board = new Board3();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        assertNoCollisions(board, 4);
    }

    private void assertNoCollisions(Board3 board, int depth) {
        HashMap<Long, Set<Board3>> collisionMap = new HashMap<>();
        perftReset();
        perft(new LegalMoveGeneratorImpl3(), board, WHITE, depth, visitedBoard -> {
            long zobristHash = ((Board3) visitedBoard).getZobristHash();
            Set<Board3> entries = collisionMap.get(zobristHash);
            if (entries == null) {
                entries = new HashSet<>();
                entries.add(((Board3) visitedBoard).copy());
                collisionMap.put(zobristHash, entries);
            } else {
                entries.add(((Board3) visitedBoard).copy());
            }
        });

        List<Map.Entry<Long, Set<Board3>>> collisions = collisionMap.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toList());
        assertThat(collisions).isEmpty();
    }


    @Test
    public void position3() {
        Board3 board = new Board3();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        assertNoCollisions(board, 6);
    }


    @Test
    public void position4() {
        Board3 board = new Board3();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        assertNoCollisions(board, 4);

    }


}
