package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * PerfTests for fischer random chess.
 * https://www.chessprogramming.org/Chess960_Perft_Results
 */
public class PerfChess960Tests {


    @Test
    public void pos1() {

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen bqnb1rkr/pp3ppp/3ppn2/2p5/5P2/P2P4/NPP1P1PP/BQ1BNRKR w HFhf - 2 9");
        System.out.println(board.toUniCodeStr());

        perft.assertPerft(generator, board, WHITE, 1, 21);
        perft.assertPerft(generator, board, WHITE, 2, 528);
        perft.assertPerft(generator, board, WHITE, 3, 12189);
        perft.assertPerft(generator, board, WHITE, 4, 326672);
        perft.assertPerft(generator, board, WHITE, 5, 8146062);
        perft.assertPerft(generator, board, WHITE, 6, 227689589);
    }


    @Test
    public void pos447() {

        // bbrnk1qr/1pppppp1/p4n1p/8/P2P2N1/8/1PP1PPPP/BBR1NKQR w HC - 1 9 ;D1 21 ;D2 481 ;D3 11213 ;D4 279993 ;D5 7015419 ;D6 187564853

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.setVisitor(new PerftConsumer() {

            @Override
            public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {
                ((BitBoard)board).doAssertLogs();
            }
        });
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen bbrnk1qr/1pppppp1/p4n1p/8/P2P2N1/8/1PP1PPPP/BBR1NKQR w HC - 1 9");
        System.out.println(board.toUniCodeStr());

        perft.assertPerft(generator, board, WHITE, 1, 21);
        perft.assertPerft(generator, board, WHITE, 2, 481);
        perft.assertPerft(generator, board, WHITE, 3, 11213);
        perft.assertPerft(generator, board, WHITE, 4, 279993);
        perft.assertPerft(generator, board, WHITE, 5, 7015419);
        perft.assertPerft(generator, board, WHITE, 6, 187564853);
    }


    @Test
    public void pos483() {

        // q1brnknr/pp1pp1p1/8/2p2p1p/5b2/P4N2/1PPPP1PP/QBBRK1NR w hd - 0 9 ;D1 22 ;D2 675 ;D3 15778 ;D4 473994 ;D5 12077228 ;D6 368479752

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen bqnb1rkr/pp3ppp/3ppn2/2p5/5P2/P2P4/NPP1P1PP/BQ1BNRKR w HFhf - 2 9");
        System.out.println(board.toUniCodeStr());

        perft.assertPerft(generator, board, WHITE, 1, 21);
        perft.assertPerft(generator, board, WHITE, 2, 528);
        perft.assertPerft(generator, board, WHITE, 3, 12189);
        perft.assertPerft(generator, board, WHITE, 4, 326672);
        perft.assertPerft(generator, board, WHITE, 5, 8146062);
        perft.assertPerft(generator, board, WHITE, 6, 227689589);
    }

    @Test
    public void pos771() {

        // qbbrkn1r/pppppp1p/8/6p1/2P1Pn1P/6N1/PP1P1PP1/QBBRKNR1 w GDd - 3 9 ;D1 20 ;D2 532 ;D3 11581 ;D4 303586 ;D5 7512432 ;D6 202967948

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen bqnb1rkr/pp3ppp/3ppn2/2p5/5P2/P2P4/NPP1P1PP/BQ1BNRKR w HFhf - 2 9");
        System.out.println(board.toUniCodeStr());

        perft.assertPerft(generator, board, WHITE, 1, 21);
        perft.assertPerft(generator, board, WHITE, 2, 528);
        perft.assertPerft(generator, board, WHITE, 3, 12189);
        perft.assertPerft(generator, board, WHITE, 4, 326672);
        perft.assertPerft(generator, board, WHITE, 5, 8146062);
        perft.assertPerft(generator, board, WHITE, 6, 227689589);
    }
}
