package org.mattlang.jc.board;

/**
 * Conversion from algebraic to board index conversion.
 */
public class IndexConversion {

    public static String convert(int index) {
        int x = index % 8;
        int y = index / 8;
        StringBuilder b = new StringBuilder();
        b.append((char) ('a' + x));
        b.append((char) ('0' + y + 1));
        return b.toString();
    }

    public static byte parsePos(String pos) {
        int x = pos.charAt(0) - 'a';
        int y = pos.charAt(1) - '0' - 1;
        return (byte) (y * 8 + x);
    }
}
