package org.mattlang.jc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Bundles all engine configurations.
 * Either set via system properties or loaded by a property file.
 */
public class AppConfiguration {

    public static final String LOGGING_ACTIVATE = "jacky.logging.activate";
    public static final String LOGGING_DIR = "jacky.logging.dir";

    public static final String CONFIG_FILE = "jacky.config";

    public static AppConfiguration APPCONFIG = new AppConfiguration();

    private Properties properties = new Properties();

    public AppConfiguration() {
        String configFile = System.getProperty(CONFIG_FILE);
        if (configFile != null) {
            properties = loadProperty(new File(configFile));
        }
    }

    public Optional<String> getStringValue(String propName) {
        String prop = System.getProperty(propName);
        if (prop == null) {
            return Optional.ofNullable(properties.getProperty(propName));
        } else {
            return Optional.of(prop);
        }
    }

    public boolean getBooleanValue(String propName, boolean defaultVal) {
        Optional<String> prop = getStringValue(propName);
        return prop.isPresent() ? Boolean.valueOf(prop.get()) : defaultVal;

    }

    private Properties loadProperty(File file) {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            p.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("error loading config file " + file.getAbsolutePath());
        }
        return p;
    }
}
