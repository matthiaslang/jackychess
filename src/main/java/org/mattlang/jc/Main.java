package org.mattlang.jc;

import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.AppConfiguration.*;

import java.io.*;
import java.util.Optional;
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
            String configFileContent = loadFile(Main.class.getResourceAsStream(resourceFile));
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
        // either use a cofigured directory or the java.util.logging placeholder %h for user home:
        String logDir = optLogDir.isPresent() ? optLogDir.get() : "%h";
        String version = Factory.getAppProps().getProperty("version");
        String logFile = optLogFile.isPresent() ? optLogFile.get() : "jackyChess-" + version;
        configFileContent = configFileContent.replace("%h", logDir);
        configFileContent = configFileContent.replace("<FILE>", logFile);

        return configFileContent;
    }

    private static String loadFile(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(joining("\n"));
    }
}
