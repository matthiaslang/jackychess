package org.mattlang.jc.board;

public class BoardPrinter {

    public static void printBoard(Board board) {
       System.out.println(toUniCodeStr(board));
    }

    public static String toUniCodeStr(Board board) {
        StringBuilder b = new StringBuilder();
        int rowNo = 8;
        for (int row = 0; row < 8; row++) {
            b.append(rowNo);
            for (int col = 0; col < 8; col++) {
                b.append(board.getPos(row, col));
            }
            b.append("         ");
            b.append(rowNo);
            rowNo--;
            for (int col = 0; col < 8; col++) {
                b.append(board.getFigurePos(row, col).figureCharUnicode);
            }
            b.append("\n");
        }

        b.append(" abcdefgh         abcdefgh\n");
        return b.toString();
    }
}
