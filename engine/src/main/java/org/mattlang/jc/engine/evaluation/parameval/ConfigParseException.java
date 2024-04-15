package org.mattlang.jc.engine.evaluation.parameval;

public class ConfigParseException extends RuntimeException {

    public ConfigParseException(String msg) {
        super(msg);
    }

    public ConfigParseException(String msg, Throwable r) {
        super(msg, r);
    }
}
