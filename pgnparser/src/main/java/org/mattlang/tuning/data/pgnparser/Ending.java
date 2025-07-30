package org.mattlang.tuning.data.pgnparser;

public enum Ending implements Symbol {
    DRAW(Ending.PGN_DRAW_STR),

    MATE_WHITE(Ending.PGN_WHITE_WIN),

    MATE_BLACK(Ending.PGN_BLACK_WIN),
    UNTERMINATED(Ending.PGN_UNDETERMINED);

    private final String pgnStr;

    public static final String PGN_DRAW_STR = "1/2-1/2";
    public static final String PGN_WHITE_WIN = "1-0";
    public static final String PGN_BLACK_WIN = "0-1";
    public static final String PGN_UNDETERMINED = "*";

    Ending(String pgnStr) {
        this.pgnStr = pgnStr;
    }

    public static Ending match(String str) {
        switch (str) {
        case PGN_DRAW_STR:
            return Ending.DRAW;
        case PGN_WHITE_WIN:
            return Ending.MATE_WHITE;
        case PGN_BLACK_WIN:
            return Ending.MATE_BLACK;
        case PGN_UNDETERMINED:
            return Ending.UNTERMINATED;
        }
        return null;
    }

    public String getPgnResultString() {
        return pgnStr;
    }
}
