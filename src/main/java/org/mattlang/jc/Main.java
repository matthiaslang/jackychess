package org.mattlang.jc;

import java.io.IOException;

import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.uci.UciProcessor;

public class Main {

    public static void main(String[] args) throws IOException {
        UCI.instance.attachStreams(System.in, System.out);
        UciProcessor processor = new UciProcessor();
        processor.start();
    }
}
