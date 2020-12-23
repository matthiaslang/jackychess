package org.mattlang.jc.perftests;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.*;

import static org.mattlang.jc.board.Color.WHITE;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTests {


    private boolean debug = false;

    @Test
    public void initialPositionPerformanceMoveGenerator() {
        Board board = new Board();
        board.setStartPosition();

        perftReset();
        perft(new MoveGeneratorImpl(), board, WHITE, 6);

    }

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Board board = new Board();
        board.setStartPosition();

        LegalMoveGeneratorImpl2 generator = new LegalMoveGeneratorImpl2();

        assertPerft(generator, board, WHITE, 1, 20, 0, 0);

        assertPerft(generator, board, WHITE, 2, 400, 0, 0);

        assertPerft(generator, board, WHITE, 3, 8902, 34, 0);

        assertPerft(generator, board, WHITE, 4, 197281, 1576, 0);

        assertPerft(generator, board, WHITE, 5, 4865609, 82719, 258);

        // takes too long for unit test
//      assertPerft(generator, board, WHITE, 6, 119060324, 2812008);

    }


    @Test
    public void initialPositionPerformanceOptBoardAndLegalMoves() {
        Board2 board = new Board2();
        board.setStartPosition();

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        LegalMoveGeneratorImpl3 legalMoveGen = new LegalMoveGeneratorImpl3();

        assertPerft(legalMoveGen, board, WHITE, 1, 20, 0, 0);

        assertPerft(legalMoveGen, board, WHITE, 2, 400, 0, 0);

        assertPerft(legalMoveGen, board, WHITE, 3, 8902, 34, 0);

        assertPerft(legalMoveGen, board, WHITE, 4, 197281, 1576, 0);

        // todo does not match... why?
//        assertPerft(legalMoveGen, board, Color.WHITE, 5, 4865609, 82719, 258);

        Factory.setDefaults(Factory.createDefaultParameter());
    }

    @Test
    public void position2() {
        Board board = new Board();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        assertPerft(new LegalMoveGeneratorImpl2(), board, WHITE, 1, 48, 8, 0);

        // todo not working, maybe because of castle rights checking missing
        //assertPerft(new LegalMoveGeneratorImpl2(), board, WHITE, 2, 2039, 351, 1);
    }


    @Test
    public void position3() {
        Board board = new Board();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        LegalMoveGeneratorImpl2 generator = new LegalMoveGeneratorImpl2();

        assertPerft(generator, board, WHITE, 1, 14, 1, 0);

        assertPerft(generator, board, WHITE, 2, 191, 14, 0);

        assertPerft(generator, board, WHITE, 3, 2812, 209, 2);
    }


    @Test
    public void position4() {
        Board board = new Board();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        LegalMoveGeneratorImpl2 generator = new LegalMoveGeneratorImpl2();

        assertPerft(generator, board, WHITE, 1, 6, 0, 0);

        // todo does not match...
        //assertPerft(generator, board, WHITE, 2, 264, 87, 0);

    }

    int nodes = 0;
    int captures = 0;
    int ep = 0;

    void perftReset() {
        nodes = 0;
        captures = 0;
        ep = 0;
    }


    private void assertPerft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth,
                             int expectedNodes, int expectedCaptures, int expectedEP) {
        perftReset();
        perft(generator, board, color, depth);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(nodes).as("Leave Nodes").isEqualTo(expectedNodes);
        softly.assertThat(captures).as("Captures").isEqualTo(expectedCaptures);
        softly.assertThat(ep).as("En Passant").isEqualTo(expectedEP);
        softly.assertAll();
    }

    void perft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth) {

        if (depth == 0) {
            nodes++;
            return;
        }

        MoveList moves = generator.generate(board, color);
        for (MoveCursor moveCursor : moves) {

            if (depth == 1) {
                if (moveCursor.isCapture()) {
                    captures++;
                    if (debug == true) {
                        System.out.println("color: " + color + ", capture " + captures + ": " + moveCursor.getMove().toStr() + " on board: ");
                        System.out.println(board.toUniCodeStr());
                    }

                }
                if (moveCursor.getMove() instanceof EnPassantMove) {
                    ep++;
                }
            }
            moveCursor.move(board);

            perft(generator, board, color.invert(), depth - 1);
            moveCursor.undoMove(board);
        }
    }
}
