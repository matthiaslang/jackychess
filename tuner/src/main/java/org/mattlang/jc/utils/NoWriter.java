package org.mattlang.jc.utils;

import java.io.IOException;
import java.io.Writer;

/**
 * A "nothing" writer, to actually disable output writing.
 */
public class NoWriter extends Writer {

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
