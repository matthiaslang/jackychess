package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.moves.MoveImpl;

public class CastlingChess960Test {

    @Test
    public void test01() {
        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen rk2r3/8/8/8/8/8/8/RK2R3 b KQkq - 0 14");
        BoardPrinter.printBoard(board);

        board.domove(MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteShort()));
        board.domove(MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackShort()));
        BoardPrinter.printBoard(board);

        assertThat(board.getFigure(parsePos("f1"))).isEqualTo(W_Rook);
        assertThat(board.getFigure(parsePos("g1"))).isEqualTo(W_King);

        assertThat(board.getFigure(parsePos("f8"))).isEqualTo(B_Rook);
        assertThat(board.getFigure(parsePos("g8"))).isEqualTo(B_King);

    }


    @Test
    public void test02() {
        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen rk2r3/8/8/8/8/8/8/RK2R3 b KQkq - 0 14");
        BoardPrinter.printBoard(board);

        board.domove(MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteLong()));
        board.domove(MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackLong()));
        BoardPrinter.printBoard(board);

        assertThat(board.getFigure(parsePos("c1"))).isEqualTo(W_King);
        assertThat(board.getFigure(parsePos("d1"))).isEqualTo(W_Rook);

        assertThat(board.getFigure(parsePos("c8"))).isEqualTo(B_King);
        assertThat(board.getFigure(parsePos("d8"))).isEqualTo(B_Rook);

    }


    @Test
    public void testIssue01() {
        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen brkbnqnr/pppppppp/8/8/8/8/PPPPPPPP/BRKBNQNR w HBhb - 0 1 moves d2d4 g8f6 c2c4 g7g6 e1d3 b7b6 g1f3 e7e6 b2b3 b6b5 c4c5 a8e4 d1c2 c7c6 a1b2 f6d5 d3e5 f7f5 e2e3 e8f6 f3g5 e4c2 c1c2 f6g4 e5g4 d8g5 g4e5 g5e3 b1d1 f8e7 c2b1 e3f4 e5d3 f4g5 f1e2 a7a5 g2g3 c8h8");
        BoardPrinter.printBoard(board);

        // last move was black castling move (g) encoded as king captures rook:
        assertThat(board.getFigure(parsePos("f8"))).isEqualTo(B_Rook);
        assertThat(board.getFigure(parsePos("g8"))).isEqualTo(B_King);

    }


}
