package org.mattlang.jc.uci;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;

public class FenParserTest {

    @Test
    public void testFenParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3", board);

        System.out.println(board.toUniCodeStr());
    }
}