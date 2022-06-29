package org.mattlang.tuning.data.pgnparser;

public class PgnParserException extends RuntimeException {

    public PgnParserException(String message) {
        super(message);
    }

    public PgnParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
