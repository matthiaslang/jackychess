package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.IndexConversion;
import org.mattlang.jc.board.bitboard.BitBoard;

public class FenParserTest {

    @Test
    public void testFenParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3",
                board);

        System.out.println(board.toUniCodeStr());
    }

    @Test
    public void testKnightPromotionParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen 4k3/6PP/8/8/8/8/ppp3K1/8 b - - 0 1 moves c2c1q g7g8n", board);
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigurePos(0, 6)).isEqualTo(Figure.W_Knight);

    }

    @Test
    public void testKnightPromotionWithCaptureParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen 4k2q/6PP/8/8/8/8/ppp3K1/8 b - - 0 1 moves c2c1q g7h8n", board);
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigure(IndexConversion.parsePos("h8"))).isEqualTo(Figure.W_Knight);

    }

    @Test
    public void testRookPromotionParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen 4k3/6PP/8/8/8/8/ppp3K1/8 b - - 0 1 moves c2c1q g7g8r", board);
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigurePos(0, 6)).isEqualTo(Figure.W_Rook);

    }

    @Test
    public void testBishopPromotionParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen 4k3/6PP/8/8/8/8/ppp3K1/8 b - - 0 1 moves c2c1q g7g8b", board);
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigurePos(0, 6)).isEqualTo(Figure.W_Bishop);

    }

    @Test
    public void testQueenPromotionParsing() {
        FenParser p = new FenParser();
        BoardRepresentation board = new BitBoard();
        p.setPosition("position fen 4k3/6PP/8/8/8/8/ppp3K1/8 b - - 0 1 moves c2c1q g7g8q", board);
        System.out.println(board.toUniCodeStr());

        assertThat(board.getFigurePos(0, 6)).isEqualTo(Figure.W_Queen);

    }

}