package org.mattlang.jc.poc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Test;

public class LoggerTest {

    Logger logger = Logger.getLogger("UCILOG");

    @Test
    public void javautilloggingtest() throws IOException {
        initLogging();
        logger.setLevel(Level.ALL);

        logger.log(Level.SEVERE, "severe thing!");

        logger.log(Level.INFO, "another log");
    }

    private void initLogging() {
        Logger logger = Logger.getAnonymousLogger();
        LogManager manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(LoggerTest.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }
}
