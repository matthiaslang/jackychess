package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class IntegerNumber implements Symbol {

    private final int i;

    public IntegerNumber(int i) {
        this.i = i;
    }
}
