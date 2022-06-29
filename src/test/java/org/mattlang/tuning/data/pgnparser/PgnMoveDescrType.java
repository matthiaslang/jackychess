package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public enum PgnMoveDescrType {

    NORMAL(false),

    CASTLING_SHORT(false),

    CASTLING_LONG(false),

    DRAW(true),

    MATE_WHITE(true),

    MATE_BLACK(true);

    private final boolean end;

    PgnMoveDescrType(boolean end) {
        this.end = end;
    }
}
