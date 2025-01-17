package org.mattlang.jc.perftests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.PerfTests.DEFAULT_SUPPLIER;

import java.util.*;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.zobrist.Zobrist;

/**
 * PerfTests for zobrist hashing.
 *
 */
@Category(SlowTests.class)
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
                    Perft perft = new Perft(DEFAULT_SUPPLIER);
                    perft.setVisitor((visitedBoard, c, d, cursor) -> {
                        int hash = visitedBoard.hashCode();
                        i[0] = hash;
                    });

                    perft.perft( board, WHITE, 6);
                });

        StopWatch zobristMeasure = benchmark(
                "board3 zobrist hash",
                () -> {
                    BitBoard board = new BitBoard();
                    board.setStartPosition();
                    Perft perft = new Perft(DEFAULT_SUPPLIER);
                    perft.setVisitor((visitedBoard, c, d, cursor) -> {
                        long zobristHash = visitedBoard.getZobristHash();
                        l[0] = zobristHash;
                    });

                    perft.perft( board, WHITE, 6);
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

        assertNoCollisions(board, 4);
    }

    private void assertNoCollisions(BitBoard board, int depth) {
        HashMap<Long, Set<String>> collisionMap = new HashMap<>();
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor((visitedBoard, c, d, cursor) -> {

            // check pawn zobrist hash : incremental must be equal to calculated one:
            long zobristPawnHash = visitedBoard.getPawnKingZobristHash();
            long zobristPawnFromScratch = Zobrist.hashPawnsAndKings(visitedBoard);
            if (zobristPawnHash!=zobristPawnFromScratch){
                System.out.println(visitedBoard.toUniCodeStr());
            }
            assertThat(zobristPawnHash).isEqualTo(zobristPawnFromScratch);

            // check zobrist hash: incremental must be equal to calculated one:
            long zobristHash = visitedBoard.getZobristHash();
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

        perft.perft( board, WHITE, depth);

        List<Map.Entry<Long, Set<String>>> collisions =
                collisionMap.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toList());
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

        assertNoCollisions(board, 4);

    }


}
