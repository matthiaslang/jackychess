package org.mattlang.jc;

import java.io.IOException;

import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.uci.UciProcessor;
import org.mattlang.jc.util.Logging;

public class Main {

    public static void main(String[] args) throws IOException {
        Logging.initLogging();
        UCI.instance.attachStreams(System.in, System.out);
        UciProcessor processor = new UciProcessor();
        processor.start();
    }

}
