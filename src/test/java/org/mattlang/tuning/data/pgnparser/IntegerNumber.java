package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class IntegerNumber extends TextualSymbol {

    private final int number;

    public IntegerNumber(String str, TextPosition textPosition) {
        super(str, textPosition);
        this.number = Integer.parseInt(str);
    }
}
