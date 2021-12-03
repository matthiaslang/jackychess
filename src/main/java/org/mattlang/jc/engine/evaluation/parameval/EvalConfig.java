package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.parameval.MobLinFun.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mattlang.jc.Factory;

import lombok.Getter;

/**
 * Bundles configuration for parameterized evaluation functions.
 */
public class EvalConfig {

    private final String configName;

    private final Properties properties;
    @Getter
    private final String configDir;

    public EvalConfig() {
        configName = Factory.getDefaults().getConfig().evaluateParamSet.getValue().name().toLowerCase();

        // read in all configuration for all the evaluation components:
        configDir = "/config/" + configName + "/";

        properties = loadPropertyFile(configDir + "config.properties");
    }

    public int getIntProp(String propName) {
        return parseInt(getProp(propName), propName);
    }

    public static int parseInt(String str, String name) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            throw new ConfigParseException("Cant parse " + name + ": " + str, nfe);
        }
    }

    public MobLinFun parseFun(String propName) {
        try {
            return parse(getProp(propName));
        } catch (RuntimeException r) {
            throw new ConfigParseException("Error parsing Property " + propName, r);
        }
    }

    public KingAttackFun parseKAFun(String propName) {
        try {
            return KingAttackFun.parse(getProp(propName));
        } catch (RuntimeException r) {
            throw new ConfigParseException("Error parsing Property " + propName, r);
        }
    }

    private String getProp(String propName) {
        String propVal = properties.getProperty(propName);
        if (propVal == null) {
            throw new ConfigParseException("No Property  " + propName + " defined!");
        }
        return propVal;
    }

    public static Properties loadPropertyFile(String resourceFile) {
        Properties properties = new Properties();
        InputStream is =
                ParameterizedMobilityEvaluation.class.getResourceAsStream(resourceFile);
        if (is == null) {
            throw new ConfigParseException("Could not find mobility properties resource file " + resourceFile);
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new ConfigParseException("Could not read mobility properties from resource file " + resourceFile,
                    e);
        }
        return properties;
    }
}
