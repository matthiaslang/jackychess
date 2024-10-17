package org.mattlang.jc.util;

import static java.util.logging.Level.parse;
import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.AppConfiguration.*;

import java.io.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.mattlang.jc.AppConfiguration;

public class Logging {

    public static final String GOBBLER_LOGNAME = "Gobbler";

    public static void initLogging() {
        if (APPCONFIG.getBooleanValue(LOGGING_ACTIVATE, false)) {
            readLoggerConfig("/logging.properties");
        } else {
            readLoggerConfig("/nologging.properties");
        }

        // activate uci logging if flag is activated
        if (APPCONFIG.getBooleanValue(LOG_UCI, false)) {
            Logger gobblerlogger = Logger.getLogger(GOBBLER_LOGNAME);
            gobblerlogger.setLevel(Level.FINE);
        }
    }

    public static void initLogging(String resourceFileLogProperties) {
        readLoggerConfig(resourceFileLogProperties);
    }

    private static void readLoggerConfig(String resourceFile) {
        LogManager manager = LogManager.getLogManager();
        try {
            manager.reset();
            String configFileContent = loadFile(Logging.class.getResourceAsStream(resourceFile));
            configFileContent = replaceVariables(configFileContent);
            ByteArrayInputStream bis = new ByteArrayInputStream(configFileContent.getBytes());
            manager.readConfiguration(bis);
        } catch (IOException e) {
            throw new RuntimeException("Error reading logger config file: " + resourceFile);
        }
    }

    private static String replaceVariables(String configFileContent) {
        Optional<String> optLogDir = APPCONFIG.getStringValue(LOGGING_DIR);
        Optional<String> optLogFile = APPCONFIG.getStringValue(LOGGING_FILE);
        Optional<String> optLogLevel = APPCONFIG.getStringValue(LOGGING_LEVEL);

        // either use a cofigured directory or the java.util.logging placeholder %h for user home:
        String logDir = optLogDir.isPresent() ? optLogDir.get() : "%h";
        String version = AppConfiguration.getAppProps().getProperty("version");
        String logFile = optLogFile.isPresent() ? optLogFile.get() : "jackyChess-" + version;
        String logLevelRow =
                optLogLevel.isPresent() ? ".level=" + parse(optLogLevel.get()) : ".level=INFO";

        configFileContent = configFileContent.replace("%h", logDir);
        configFileContent = configFileContent.replace("<FILE>", logFile);
        configFileContent = configFileContent.replace(".level=INFO", logLevelRow);

        return configFileContent;
    }

    private static String loadFile(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(joining("\n"));
    }
}
