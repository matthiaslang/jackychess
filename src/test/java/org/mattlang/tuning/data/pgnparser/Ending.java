package org.mattlang.tuning.data.pgnparser;

public enum Ending implements Symbol {
    DRAW,

    MATE_WHITE,

    MATE_BLACK,
    UNTERMINATED;

    public static Ending match(String str) {
        switch (str) {
        case "1/2-1/2":
            return Ending.DRAW;
        case "1-0":
            return Ending.MATE_WHITE;
        case "0-1":
            return Ending.MATE_BLACK;
        case "*":
            return Ending.UNTERMINATED;
        }
        return null;
    }
}
