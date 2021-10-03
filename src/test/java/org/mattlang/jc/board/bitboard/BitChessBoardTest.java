package org.mattlang.jc.board.bitboard;

import org.junit.Test;

public class BitChessBoardTest {

    @Test
    public void testToStr() {
        System.out.println(BitChessBoard.toStr(1L));
        System.out.println(BitChessBoard.toStrBoard(1L));
    }
}