package org.mattlang.jc;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.uci.UciProcessor;

public class Main {

    public static void main(String[] args) throws IOException {
        initLogging();
        UCI.instance.attachStreams(System.in, System.out);
        UciProcessor processor = new UciProcessor();
        processor.start();
    }

    private static void initLogging() {
        java.util.logging.Logger logger = Logger.getAnonymousLogger();
        LogManager manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }
}
