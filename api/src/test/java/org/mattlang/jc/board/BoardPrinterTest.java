package org.mattlang.jc.board;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;

public class BoardPrinterTest {

    @Test
    public void printOutputTest() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        BoardPrinter.printBoard(board);
        BoardPrinter.printSmallBoard(board);
        
        board.setFenPosition("position fen K7/8/8/2Q5/2r5/3b4/8/7k w - - 1 56 ");
        BoardPrinter.printBoard(board);
        BoardPrinter.printSmallBoard(board);

    }

}