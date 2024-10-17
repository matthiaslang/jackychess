package org.mattlang.jc;

import static java.util.logging.Level.INFO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import org.mattlang.jc.util.PropertyConfig;

/**
 * Bundles all engine configurations.
 * Either set via system properties or loaded by a property file.
 */
public class AppConfiguration {

    private static final Logger LOGGER = Logger.getLogger(AppConfiguration.class.getName());

    public static final String LOGGING_ACTIVATE = "jacky.logging.activate";
    public static final String LOGGING_DIR = "jacky.logging.dir";
    public static final String LOGGING_LEVEL = "jacky.logging.level";
    public static final String LOGGING_FILE = "jacky.logging.file";

    /** flag to activate uci logging (independently of the general log level of Handlers). */
    public static final String LOG_UCI="jacky.logging.logUCI";

    public static final String CONFIG_FILE = "jacky.config";

    public static final AppConfiguration APPCONFIG = new AppConfiguration();

    private PropertyConfig properties = new PropertyConfig();

    /**
     * static app properties containing build information.
     */
    private static Properties appProps = loadAppProps();

    public AppConfiguration() {
        String configFile = System.getProperty(CONFIG_FILE);
        if (configFile != null) {
            LOGGER.log(INFO, "loading config file {0}", configFile);
            properties = PropertyConfig.loadFromFile(configFile);
        }
    }

    public Optional<String> getStringValue(String propName) {
        return properties.getOptProperty(propName);
    }

    public boolean getBooleanValue(String propName, boolean defaultVal) {
        return properties.getOptBoolProp(propName, defaultVal);
    }


    private static Properties loadAppProps() {
        InputStream in = AppConfiguration.class.getResourceAsStream("/app.properties");
        appProps = new Properties();
        try {
            appProps.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appProps;
    }

    public static Properties getAppProps() {
        return appProps;
    }
}
