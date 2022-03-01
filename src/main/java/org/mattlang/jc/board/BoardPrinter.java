package org.mattlang.jc.board;

import java.util.function.BiFunction;

public class BoardPrinter {

    public static void printBoard(BoardRepresentation board) {
       System.out.println(toUniCodeStr(board));
    }

    public static String toUniCodeStr(BoardRepresentation board) {
        // two variants, since the unicode characters are weird on some consoles...
        String s1 = toStr((row,col) -> board.getPos(row,col));
        String s2 =  toStr((row,col) -> board.getFigurePos(row,col).figureCharUnicode);

        // combine the two board prints:
        StringBuilder b = new StringBuilder();
        String[] r1 = s1.split("\n");
        String[] r2 = s2.split("\n");
        for (int i=0; i<r1.length; i++) {
            b.append(r1[i] + "          " + r2[i]);
            b.append("\n");
        }
        return b.toString();
    }

    public static String toStr(BiFunction<Integer,Integer, Character> posFunction) {
        StringBuilder b = new StringBuilder();
        int rowNo = 8;
        for (int row = 0; row < 8; row++) {
            addSeparator(b);
            b.append(rowNo);

            rowNo--;
            for (int col = 0; col < 8; col++) {
                b.append("| ");
                b.append(posFunction.apply(row, col));
                b.append(" ");
            }
            b.append("|\n");
        }
        addSeparator(b);
        b.append("   a   b   c   d   e   f   g   h  \n");
        return b.toString();
    }

    private static void addSeparator(StringBuilder b) {
        b.append(" ");
        for (int i = 0; i < 8; i++) {
            b.append("+---");
        }
        b.append("+\n");
    }

    public static void printSmallBoard(BoardRepresentation board) {
        System.out.println(toSmallBoardStr(board));
    }

    private static String toSmallBoardStr(BoardRepresentation board) {
        String s1 = toSmallStr((row, col) -> board.getPos(row, col));

        // combine the two board prints:
        StringBuilder b = new StringBuilder();
        String[] r1 = s1.split("\n");
        for (int i = 0; i < r1.length; i++) {
            b.append(r1[i] + "          ");
            b.append("\n");
        }
        return b.toString();
    }

    public static String toSmallStr(BiFunction<Integer, Integer, Character> posFunction) {
        StringBuilder b = new StringBuilder();
        int rowNo = 8;
        addSmallSeparator(b);
        for (int row = 0; row < 8; row++) {

            b.append(rowNo);

            rowNo--;
            b.append("|");
            for (int col = 0; col < 8; col++) {
                b.append(" ");
                Character figure = posFunction.apply(row, col);
                if (figure.charValue() == ' ') {
                    figure = '.';
                }
                b.append(figure);

            }
            b.append("|\n");
        }
        addSmallSeparator(b);
        b.append("   a b c d e f g h  \n");
        return b.toString();
    }

    private static void addSmallSeparator(StringBuilder b) {
        b.append(" ");
        for (int i = 0; i < 8; i++) {
            b.append("-+");
        }
        b.append("+\n");
    }
}
