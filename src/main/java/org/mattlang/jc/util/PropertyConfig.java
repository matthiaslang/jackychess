package org.mattlang.jc.util;

import static java.util.logging.Level.INFO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.mattlang.jc.engine.evaluation.parameval.ConfigParseException;

/**
 * Utilitly class to load properties from a property file.
 *
 * Properties are merged with system properties where system properties override the properties of the file.
 */
public class PropertyConfig {

    private static final Logger LOGGER = Logger.getLogger(PropertyConfig.class.getName());

    private Properties properties = new Properties();

    public PropertyConfig() {
    }

    /**
     * Creates an empty property configuration.
     */
    public PropertyConfig(Properties properties) {
        this.properties = properties;
    }

    public static PropertyConfig loadFromFile(String configFile) {
        LOGGER.log(INFO, "loading config file {0}", configFile);
        return new PropertyConfig(loadProperty(new File(configFile)));
    }

    public static PropertyConfig loadFromResourceFile(String configResourceFile) {
        LOGGER.log(INFO, "loading config resource file {0}", configResourceFile);
        return new PropertyConfig(loadPropertyFromResourceFile(configResourceFile));
    }

    /**
     * Returns the property value. Throws an exception if the property is not defined.
     *
     * @param propName
     * @return
     */
    public String getProperty(String propName) {
        String propVal = System.getProperty(propName);
        if (propVal != null) {
            return propVal;
        }
        propVal = properties.getProperty(propName);
        if (propVal == null) {
            throw new ConfigParseException("No Property  " + propName + " defined!");
        }
        return propVal;
    }

    public boolean getBoolProp(String propName) {
        return Boolean.parseBoolean(getProperty(propName));
    }

    public int getIntProp(String propName) {
        return parseInt(getProperty(propName), propName);
    }

    public static int parseInt(String str, String name) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            throw new ConfigParseException("Cant parse " + name + ": " + str, nfe);
        }
    }

    /**
     * Returns the property, or an empty value if not defined.
     *
     * @param propName
     * @return
     */
    public Optional<String> getOptProperty(String propName) {
        String prop = System.getProperty(propName);
        if (prop == null) {
            return Optional.ofNullable(properties.getProperty(propName));
        } else {
            return Optional.of(prop);
        }
    }

    /**
     * Returns the property as boolean or false if the property is not defined.
     *
     * @param propName
     * @param defaultVal
     * @return
     */
    public boolean getOptBoolProp(String propName, boolean defaultVal) {
        Optional<String> prop = getOptProperty(propName);
        return prop.isPresent() ? Boolean.valueOf(prop.get()) : defaultVal;

    }

    public Set<String> getStringPropertyNames() {
        Set<String> all = new HashSet<>();
        all.addAll(System.getProperties().stringPropertyNames());
        all.addAll(properties.stringPropertyNames());
        return all;
    }

    private static Properties loadProperty(File file) {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            p.load(fis);

            LOGGER.log(INFO, "loaded following properties:");
            for (String propertyName : p.stringPropertyNames()) {
                LOGGER.log(INFO, "{0} = {1}", new Object[] { propertyName, p.getProperty(propertyName) });
            }
        } catch (IOException e) {
            throw new RuntimeException("error loading config file " + file.getAbsolutePath());
        }
        return p;
    }

    public static Properties loadPropertyFromResourceFile(String resourceFile) {
        Properties properties = new Properties();
        InputStream is =
                PropertyConfig.class.getResourceAsStream(resourceFile);
        if (is == null) {
            throw new ConfigParseException("Could not find properties resource file " + resourceFile);
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new ConfigParseException("Could not read properties from resource file " + resourceFile, e);
        }
        return properties;
    }
}
