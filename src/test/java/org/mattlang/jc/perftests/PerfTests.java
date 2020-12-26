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
        Board2 board = new Board2();
        board.setStartPosition();

        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        perftInitialPosition(board, generator);

    }

    private void perftInitialPosition(BoardRepresentation board, LegalMoveGenerator generator) {
        assertPerft(generator, board, WHITE, 1, 20, 0, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 400, 0, 0, 0, 0);

        assertPerft(generator, board, WHITE, 3, 8902, 34, 0, 0, 0);

        assertPerft(generator, board, WHITE, 4, 197281, 1576, 0, 0, 0);

        assertPerft(generator, board, WHITE, 5, 4865609, 82719, 258, 0, 0);

        // takes too long for unit test
//      assertPerft(generator, board, WHITE, 6, 119060324, 2812008, 0, 0);
    }


    @Test
    public void initialPositionPerformanceOptBoardAndLegalMoves() {
        Board2 board = new Board2();
        board.setStartPosition();

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        LegalMoveGeneratorImpl3 legalMoveGen = new LegalMoveGeneratorImpl3();

        perftInitialPosition(board, legalMoveGen);

        Factory.setDefaults(Factory.createDefaultParameter());
    }

    @Test
    public void position2() {
        Board2 board = new Board2();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        LegalMoveGeneratorImpl3 generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 48, 8, 0, 2, 0);

        assertPerft(generator, board, WHITE, 2, 2039, 351, 1, 91, 0);

        assertPerft(generator, board, WHITE, 3, 97862, 17102, 45, 3162, 0);

        assertPerft(generator, board, WHITE, 4, 4085603, 757163, 1929, 128013, 15172);

        // todo takes rel long: diffs in castle rights; need to track moved rooks, kings...
        //assertPerft(generator, board, WHITE, 5, 193690690, 35043416, 73365, 4993637, 8392);


    }


    @Test
    public void position3() {
        Board2 board = new Board2();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());

        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 14, 1, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 191, 14, 0, 0, 0);

        assertPerft(generator, board, WHITE, 3, 2812, 209, 2, 0, 0);

        assertPerft(generator, board, WHITE, 4, 43238, 3348, 123, 0, 0);

        assertPerft(generator, board, WHITE, 5, 674624, 52051, 1165, 0, 0);

        assertPerft(generator, board, WHITE, 6, 11030083, 940350, 33325, 0, 7552);
    }


    @Test
    public void position4() {
        Board2 board = new Board2();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        LegalMoveGeneratorImpl3 generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 6, 0, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 264, 87, 0, 6, 48);

        assertPerft(generator, board, WHITE, 3, 9467, 1021, 4, 0, 120);

        assertPerft(generator, board, WHITE, 4, 422333, 131393, 0, 7795, 60032);

    }

    int nodes = 0;
    int captures = 0;
    int ep = 0;
    int castles = 0;
    int promotion = 0;

    void perftReset() {
        nodes = 0;
        captures = 0;
        ep = 0;
        castles = 0;
        promotion = 0;
    }


    private void assertPerft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth,
                             int expectedNodes, int expectedCaptures, int expectedEP, int expectedCastles, int expectedPromotions) {
        perftReset();
        perft(generator, board, color, depth);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(nodes).as("Leave Nodes").isEqualTo(expectedNodes);
        softly.assertThat(captures).as("Captures").isEqualTo(expectedCaptures);
        softly.assertThat(ep).as("En Passant").isEqualTo(expectedEP);
        softly.assertThat(castles).as("Castles (Rochade)").isEqualTo(expectedCastles);
        softly.assertThat(promotion).as("Promotions").isEqualTo(expectedPromotions);
        softly.assertAll();
    }

    void perft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth) {

        if (depth == 0) {
            nodes++;
            if (debug == true) {
                System.out.println(board.toUniCodeStr());
            }
            return;
        }

        MoveList moves = generator.generate(board, color);
        for (MoveCursor moveCursor : moves) {

            if (depth == 1) {
                if (moveCursor.isCapture()) {
                    captures++;
                    if (debug == true) {
                        //System.out.println("color: " + color + ", capture " + captures + ": " + moveCursor.getMove().toStr() + " on board: ");
                        //System.out.println(board.toUniCodeStr());
                    }

                }
                if (moveCursor.getMove() instanceof EnPassantMove) {
                    ep++;
                }
                if (moveCursor.getMove() instanceof RochadeMove) {
                    castles++;
                }
                if (moveCursor.getMove() instanceof PawnPromotionMove) {
                    promotion++;
                }
            }
            moveCursor.move(board);

            perft(generator, board, color.invert(), depth - 1);
            moveCursor.undoMove(board);
        }
    }
}
