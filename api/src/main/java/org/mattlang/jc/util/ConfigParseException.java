package org.mattlang.jc.util;

public class ConfigParseException extends RuntimeException {

    public ConfigParseException(String msg) {
        super(msg);
    }

    public ConfigParseException(String msg, Throwable r) {
        super(msg, r);
    }
}
