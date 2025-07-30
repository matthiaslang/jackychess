package org.mattlang.tuning.data.builder;

import org.mattlang.tuning.data.pgnparser.TextPosition;

public class EmptyPos implements TextPosition {

    public static final TextPosition EMPTYPOS = new EmptyPos();

    @Override
    public int getLineNo() {
        return 0;
    }

    @Override
    public int getColNo() {
        return 0;
    }

}
