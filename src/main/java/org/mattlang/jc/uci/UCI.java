package org.mattlang.jc.uci;

import java.io.IOException;

public class UCI extends Gobbler {

    public static final UCI instance = new UCI();

    private UCI() {
        super("UCI Singleton");
    }

    public void attachStreams() throws IOException {
        attachStreams(System.in, System.out);
    }

}
