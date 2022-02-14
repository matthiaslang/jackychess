package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.perft;
import static org.mattlang.jc.perftests.Perft.perftReset;

import java.util.*;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.attic.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.attic.movegenerator.MoveGeneratorImpl3;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.zobrist.Zobrist;

/**
 * PerfTests for zobrist hashing.
 *
 */
public class ZobristPerfTests {

    @Test
    public void sillyComparePureHash() {

        final int[] i = new int[1];
        final long[] l = new long[1];

        StopWatch hashMeasure = benchmark(
                "board2 normal hash",
                () -> {
                    BitBoard board = new BitBoard();
                    board.setStartPosition();
                    perftReset();

                    for(int j=0; j<100000000; j++)  {
                        int hash = board.hashCode();
                        i[0] = hash;
                    }
                });


        StopWatch zobristMeasure = benchmark(
                "board3 zobrist hash",
                () -> {
                    BitBoard board = new BitBoard();
                    board.setStartPosition();
                    perftReset();
                    for(int j=0; j<100000000; j++)  {
                        long zobristHash = board.getZobristHash();
                        l[0] = zobristHash;
                    }
                });

        System.out.println("zobrist time: " + zobristMeasure.toString());
        System.out.println("hash    time: " + hashMeasure.toString());

        Assertions.assertThat(zobristMeasure.getDuration()).isLessThan(hashMeasure.getDuration());

    }



    @Test
    public void compareSpeed() {

        final int[] i = new int[1];
        final long[] l = new long[1];

        StopWatch hashMeasure = benchmark(
                "board2 normal hash",
                () -> {
                    BitBoard board = new BitBoard();
            board.setStartPosition();
            perftReset();

            perft(new LegalMoveGeneratorImpl3(), board, WHITE, 5, (visitedBoard,c,d) -> {
                int hash = visitedBoard.hashCode();
                i[0] = hash;
            });
        });


        StopWatch zobristMeasure = benchmark(
                "board3 zobrist hash",
                () -> {
                    BitBoard board = new BitBoard();
            board.setStartPosition();
            perftReset();
            perft(new LegalMoveGeneratorImpl3(), board, WHITE, 5, (visitedBoard,c,d) -> {
                long zobristHash = visitedBoard.getZobristHash();
                l[0] = zobristHash;
            });
        });

        System.out.println("zobrist time: " + zobristMeasure.toString());
        System.out.println("hash    time: " + hashMeasure.toString());

        Assertions.assertThat(zobristMeasure.getDuration()).isLessThan(hashMeasure.getDuration());

    }

    @Test
    public void initialPosZobristHashUnique() {
        BitBoard board = new BitBoard();
        board.setStartPosition();

        assertNoCollisions(board, 5);

    }


    @Test
    public void position2() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        assertNoCollisions(board, 4);
    }

    private void assertNoCollisions(BitBoard board, int depth) {
        HashMap<Long, Set<String>> collisionMap = new HashMap<>();
        perftReset();
        perft(new LegalMoveGeneratorImpl3(), board, WHITE, depth, (visitedBoard, c, d) -> {
            BitBoard visitedBoard1 = (BitBoard) visitedBoard;
            long zobristHash = visitedBoard1.getZobristHash();
            long zobristFromScratch = Zobrist.hash(visitedBoard);
            assertThat(zobristHash).isEqualTo(zobristFromScratch);

            Set<String> entries = collisionMap.get(zobristHash);
            if (entries == null) {
                entries = new HashSet<>();
                entries.add("visitedBoard1.copy()");
                collisionMap.put(zobristHash, entries);
            } else {
                entries.add("visitedBoard1.copy()");
            }
        });

        List<Map.Entry<Long, Set<String>>> collisions = collisionMap.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toList());
        assertThat(collisions).isEmpty();
    }


    @Test
    public void position3() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        assertNoCollisions(board, 6);
    }


    @Test
    public void position4() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        assertNoCollisions(board, 4);

    }


}
