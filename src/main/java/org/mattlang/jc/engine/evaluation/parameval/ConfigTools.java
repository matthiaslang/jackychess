package org.mattlang.jc.engine.evaluation.parameval;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigTools {

    public static Properties loadPropertyFile(String resourceFile) {
        Properties properties = new Properties();
        InputStream is =
                ParameterizedMobilityEvaluation.class.getResourceAsStream(resourceFile);
        if (is == null) {
            throw new IllegalArgumentException("Could not find mobility properties resource file " + resourceFile);
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read mobility properties from resource file " + resourceFile,
                    e);
        }
        return properties;
    }

    public static int getIntProp(Properties properties, String propName) {
        String strProp = properties.getProperty(propName);
        if (strProp == null) {
            throw new IllegalArgumentException("Property " + propName + " not found!");
        }

        return parseInt(strProp, propName);
    }

    public static int parseInt(String str, String name) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Cant parse " + name + ": " + str, nfe);
        }
    }
}
