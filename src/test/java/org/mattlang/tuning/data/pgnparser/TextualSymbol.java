package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

public class TextualSymbol implements Symbol, TextPosition {

    @Getter
    private final String text;

    private final int lineNo;

    private final int colNo;

    public TextualSymbol(String text, TextPosition textPosition) {
        this.text = text;
        this.lineNo = textPosition.getLineNo();
        this.colNo = textPosition.getColNo();
    }

    @Override
    public int getLineNo() {
        return lineNo;
    }

    @Override
    public int getColNo() {
        return colNo;
    }
}
