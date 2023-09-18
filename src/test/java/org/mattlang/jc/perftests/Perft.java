package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveIterationPreparer;

import lombok.Getter;
import lombok.Setter;

/**
 * PerfTests Methods
 */
public class Perft {

    private boolean debug = false;

    int nodes = 0;
    int captures = 0;
    int ep = 0;
    int castles = 0;
    int promotion = 0;

    long start;

    @Getter
    @Setter
    PerftConsumer visitor;

    public Perft() {
        perftReset();
    }

    public void perftReset() {
        nodes = 0;
        captures = 0;
        ep = 0;
        castles = 0;
        promotion = 0;
        start = System.currentTimeMillis();
    }

    public void assertPerft(BoardRepresentation board, Color color, int depth,
            int expectedNodes, int expectedCaptures, int expectedEP, int expectedCastles, int expectedPromotions) {
        perftReset();
        perft(board, color, depth);
        long stop = System.currentTimeMillis();
        long duration = stop - start;
        if (duration != 0) {
            long nodesPerSecond = (long) nodes * 1000 / duration;
            System.out.println("Nodes/s = " + nodesPerSecond);
        }
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(nodes).as("Leave Nodes").isEqualTo(expectedNodes);
        softly.assertThat(captures).as("Captures").isEqualTo(expectedCaptures);
        softly.assertThat(ep).as("En Passant").isEqualTo(expectedEP);
        softly.assertThat(castles).as("Castles (Rochade)").isEqualTo(expectedCastles);
        softly.assertThat(promotion).as("Promotions").isEqualTo(expectedPromotions);
        softly.assertAll();
    }

    public void assertPerft(BoardRepresentation board, Color color, int depth,
            int expectedNodes) {
        perftReset();
        perft(board, color, depth);
        long stop = System.currentTimeMillis();
        long duration = stop - start;
        if (duration != 0) {
            long nodesPerSecond = (long) nodes * 1000 / duration;
            System.out.println("Nodes/s = " + nodesPerSecond);
        }
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(nodes).as("Leave Nodes").isEqualTo(expectedNodes);

        softly.assertAll();
    }

    public void perft(
            BoardRepresentation board,
            Color color,
            int depth) {

        CheckChecker checkChecker = new BBCheckCheckerImpl();
        perft( board, checkChecker, color, depth);
    }

    public void perft(
            BoardRepresentation board,
            CheckChecker checkChecker,
            Color color,
            int depth) {

        if (depth == 0) {
            nodes++;
            if (debug == true) {
                System.out.println(board.toUniCodeStr());
            }
            return;
        }
        MoveIterationPreparer moveIterationPreparer = SearchThreadContexts.CONTEXTS.getContext(0).getMoveIterationPreparer(depth);
        moveIterationPreparer.prepare(SearchThreadContexts.CONTEXTS.getContext(0), GenMode.NORMAL,
                board, color, 0, 0, 0);


        try (MoveBoardIterator iterator = moveIterationPreparer.iterateMoves()) {


            while (iterator.doNextValidMove()) {

                if (depth == 1) {
                    if (iterator.isCapture()) {
                        captures++;
                    }
                    if (iterator.isEnPassant()) {
                        ep++;
                    }
                    if (iterator.isCastling()) {
                        castles++;
                    }
                    if (iterator.isPromotion()) {
                        promotion++;
                    }
                }

                if (visitor != null) {
                    visitor.accept(board, color, depth, iterator);
                }
                perft(board, checkChecker, color.invert(), depth - 1);

            }
        }

    }

    public void perftInitialPosition() {
        Factory.setDefaults(Factory.createStable());
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        assertPerft(board, WHITE, 1, 20, 0, 0, 0, 0);

        assertPerft(board, WHITE, 2, 400, 0, 0, 0, 0);

        assertPerft(board, WHITE, 3, 8902, 34, 0, 0, 0);

        assertPerft(board, WHITE, 4, 197281, 1576, 0, 0, 0);

        assertPerft(board, WHITE, 5, 4865609, 82719, 258, 0, 0);

        assertPerft(board, WHITE, 6, 119060324, 2812008, 5248, 0, 0);
    }

    public void position2() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        assertPerft(board, WHITE, 1, 48, 8, 0, 2, 0);

        assertPerft(board, WHITE, 2, 2039, 351, 1, 91, 0);

        assertPerft(board, WHITE, 3, 97862, 17102, 45, 3162, 0);

        assertPerft(board, WHITE, 4, 4085603, 757163, 1929, 128013, 15172);

        assertPerft(board, WHITE, 5, 193690690, 35043416, 73365, 4993637, 8392);

    }

    public void position3() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        assertPerft(board, WHITE, 1, 14, 1, 0, 0, 0);

        assertPerft(board, WHITE, 2, 191, 14, 0, 0, 0);

        assertPerft(board, WHITE, 3, 2812, 209, 2, 0, 0);

        assertPerft(board, WHITE, 4, 43238, 3348, 123, 0, 0);

        assertPerft(board, WHITE, 5, 674624, 52051, 1165, 0, 0);

        assertPerft(board, WHITE, 6, 11030083, 940350, 33325, 0, 7552);

        assertPerft(board, WHITE, 7, 178633661, 14519036, 294874, 0, 140024);
    }

    @Test
    public void position4() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        assertPerft(board, WHITE, 1, 6, 0, 0, 0, 0);

        assertPerft(board, WHITE, 2, 264, 87, 0, 6, 48);

        assertPerft(board, WHITE, 3, 9467, 1021, 4, 0, 120);

        assertPerft(board, WHITE, 4, 422333, 131393, 0, 7795, 60032);

        assertPerft(board, WHITE, 5, 15833292, 2046173, 6512, 0, 329464);

        assertPerft(board, WHITE, 6, 706045033, 210369132, 212, 10882006, 81102984);

    }

}
