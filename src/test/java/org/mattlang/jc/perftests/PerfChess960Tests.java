package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;

import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.bitboard.BitBoard;
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


}
