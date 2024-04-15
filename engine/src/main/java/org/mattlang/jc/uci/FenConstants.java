package org.mattlang.jc.uci;

public class FenConstants {

    public static final String CASTLING_LONG1 = "0-0-0";
    public static final String CASTLING_LONG2 = "O-O-O";

    public static final String CASTLING_SHORT1 = "0-0";
    public static final String CASTLING_SHORT2 = "O-O";

    public static boolean isCastlingShort(String moveStr) {
        return CASTLING_SHORT1.equals(moveStr) || CASTLING_SHORT2.equals(moveStr);
    }

    public static boolean isCastlingLong(String moveStr) {
        return CASTLING_LONG1.equals(moveStr) || CASTLING_LONG2.equals(moveStr);
    }
}
