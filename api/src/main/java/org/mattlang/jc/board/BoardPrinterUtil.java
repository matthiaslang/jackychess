package org.mattlang.jc.board;

import java.util.function.BiFunction;

public class BoardPrinterUtil {

    public static String toStr(BiFunction<Integer, Integer, Character> posFunction) {
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

    public static void addSeparator(StringBuilder b) {
        b.append(" ");
        for (int i = 0; i < 8; i++) {
            b.append("+---");
        }
        b.append("+\n");
    }
}
