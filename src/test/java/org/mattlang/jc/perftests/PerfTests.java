package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.perftests.Perft.assertPerft;

import org.junit.Test;
import org.mattlang.attic.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.attic.movegenerator.MoveGeneratorImpl3;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.BBLegalMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTests {


    private boolean debug = false;


    @Test
    public void initialPositionPerformanceLegalMoves() {
        Factory.setDefaults(Factory.createStable());
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        perftInitialPosition(board, generator);

    }

    @Test
    public void initialPositionBitBoardPerformanceLegalMoves() {
        BitBoard board = new BitBoard();
        board.setStartPosition();
        LegalMoveGenerator generator = initBitBoardMoveGen();

        perftInitialPosition(board, generator);
    }

    private LegalMoveGenerator initBitBoardMoveGen() {
        Factory.getDefaults().moveGenerator.set(() -> new BBMoveGeneratorImpl());
        Factory.getDefaults().checkChecker.set(() -> new BBCheckCheckerImpl());
        LegalMoveGenerator generator = new BBLegalMoveGeneratorImpl();
        return generator;
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
    public void position2() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        LegalMoveGeneratorImpl3 generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 48, 8, 0, 2, 0);

        assertPerft(generator, board, WHITE, 2, 2039, 351, 1, 91, 0);

        assertPerft(generator, board, WHITE, 3, 97862, 17102, 45, 3162, 0);

        assertPerft(generator, board, WHITE, 4, 4085603, 757163, 1929, 128013, 15172);

        // todo takes rel long: diffs in castle rights; need to track moved rooks, kings...
        //assertPerft(generator, board, WHITE, 5, 193690690, 35043416, 73365, 4993637, 8392);


    }
    @Test
    public void position2Bitboard() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        System.out.println(board.toUniCodeStr());

        LegalMoveGenerator generator = initBitBoardMoveGen();

        assertPerft(generator, board, WHITE, 1, 48, 8, 0, 2, 0);

        assertPerft(generator, board, WHITE, 2, 2039, 351, 1, 91, 0);

        assertPerft(generator, board, WHITE, 3, 97862, 17102, 45, 3162, 0);

        assertPerft(generator, board, WHITE, 4, 4085603, 757163, 1929, 128013, 15172);

        // todo takes rel long: diffs in castle rights; need to track moved rooks, kings...
        //assertPerft(generator, board, WHITE, 5, 193690690, 35043416, 73365, 4993637, 8392);


    }

    @Test
    public void position3() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());
        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());
        LegalMoveGenerator generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 14, 1, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 191, 14, 0, 0, 0);

        assertPerft(generator, board, WHITE, 3, 2812, 209, 2, 0, 0);

        assertPerft(generator, board, WHITE, 4, 43238, 3348, 123, 0, 0);

        assertPerft(generator, board, WHITE, 5, 674624, 52051, 1165, 0, 0);

        assertPerft(generator, board, WHITE, 6, 11030083, 940350, 33325, 0, 7552);
    }

    @Test
    public void position3BitBoard() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        System.out.println(board.toUniCodeStr());
        LegalMoveGenerator generator = initBitBoardMoveGen();

        assertPerft(generator, board, WHITE, 1, 14, 1, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 191, 14, 0, 0, 0);

        assertPerft(generator, board, WHITE, 3, 2812, 209, 2, 0, 0);

        assertPerft(generator, board, WHITE, 4, 43238, 3348, 123, 0, 0);

        assertPerft(generator, board, WHITE, 5, 674624, 52051, 1165, 0, 0);

        assertPerft(generator, board, WHITE, 6, 11030083, 940350, 33325, 0, 7552);
    }


    @Test
    public void position4() {
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl3());

        LegalMoveGeneratorImpl3 generator = new LegalMoveGeneratorImpl3();

        assertPerft(generator, board, WHITE, 1, 6, 0, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 264, 87, 0, 6, 48);

        assertPerft(generator, board, WHITE, 3, 9467, 1021, 4, 0, 120);

        assertPerft(generator, board, WHITE, 4, 422333, 131393, 0, 7795, 60032);

    }

    @Test
    public void position4BitBoard() {
        BitBoard board = new BitBoard();
        board.setFenPosition("position fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        System.out.println(board.toUniCodeStr());

        LegalMoveGenerator generator = initBitBoardMoveGen();

        assertPerft(generator, board, WHITE, 1, 6, 0, 0, 0, 0);

        assertPerft(generator, board, WHITE, 2, 264, 87, 0, 6, 48);

        assertPerft(generator, board, WHITE, 3, 9467, 1021, 4, 0, 120);

        assertPerft(generator, board, WHITE, 4, 422333, 131393, 0, 7795, 60032);

    }

}
