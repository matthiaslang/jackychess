package org.mattlang.jc.tools;

import java.io.IOException;

public interface MarkdownWriteConsumer<T> {

    void accept(T t) throws IOException;
}
