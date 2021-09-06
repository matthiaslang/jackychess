package org.mattlang.jc;

import static org.mattlang.jc.AppConfiguration.APPCONFIG;
import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;

import java.io.IOException;
import java.util.logging.LogManager;

import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.uci.UciProcessor;

public class Main {

    public static void main(String[] args) throws IOException {
        initLogging();
        UCI.instance.attachStreams(System.in, System.out);
        UciProcessor processor = new UciProcessor();
        processor.start();
    }

    public static void initLogging() {
        if (APPCONFIG.getBooleanValue(LOGGING_ACTIVATE, false)) {
            readLoggerConfig("/logging.properties");
        } else {
            readLoggerConfig("/nologging.properties");
        }
    }

    private static void readLoggerConfig(String resourceFile) {
        LogManager manager = LogManager.getLogManager();
        try {
            manager.reset();
            manager.readConfiguration(Main.class.getResourceAsStream(resourceFile));
        } catch (IOException e) {
            throw new RuntimeException("Error reading logger config file: " + resourceFile);
        }
    }
}
