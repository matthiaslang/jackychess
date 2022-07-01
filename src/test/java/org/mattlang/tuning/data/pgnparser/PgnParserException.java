package org.mattlang.tuning.data.pgnparser;

public class PgnParserException extends RuntimeException {

    private int lineNo;
    private int colNo;

    public PgnParserException(String message, TextPosition textPosition) {
        super(message);
        this.lineNo = textPosition.getLineNo();
        this.colNo = textPosition.getColNo();

    }

    public PgnParserException(String message, Throwable cause, TextPosition textPosition) {
        super(message, cause);
        this.lineNo = textPosition.getLineNo();
        this.colNo = textPosition.getColNo();
    }

    @Override
    public String getMessage() {
        return lineNo + ":" + colNo + " " + super.getMessage();
    }
}
