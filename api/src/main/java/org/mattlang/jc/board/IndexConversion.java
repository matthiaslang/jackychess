package org.mattlang.jc.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
        return parsePos(pos.charAt(0), pos.charAt(1));
    }

    public static byte parsePos(char char1, char char2) {
        int x = char1 - 'a';
        int y = char2 - '0' - 1;
        return (byte) (y * 8 + x);
    }

    @AllArgsConstructor
    @Getter
    public static class MoveFromTo {

        byte from;
        byte to;
    }

    public static MoveFromTo parseMoveStr(String moveStr) {
        byte fromIndex = parsePos(moveStr.substring(0, 2));
        byte toIndex = parsePos((moveStr.substring(2, 4)));

        return new MoveFromTo(fromIndex, toIndex);
    }
}
