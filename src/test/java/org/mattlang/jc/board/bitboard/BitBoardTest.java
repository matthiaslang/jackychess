package org.mattlang.jc.board.bitboard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BitBoardTest {

    @Test
    public void testCopy() {
        BitBoard board = new BitBoard();
        board.setChess960(true);
        assertThat(board.copy().isChess960()).isTrue();

        board.setChess960(false);
        assertThat(board.copy().isChess960()).isFalse();
    }
}