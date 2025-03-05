package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.parse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FunctionParser;
import org.mattlang.jc.util.ConfigParseException;
import org.mattlang.jc.util.PropertyConfig;

import lombok.Getter;

/**
 * Bundles configuration for parameterized evaluation functions.
 */
public class EvalConfig {

    public static final String CONFIG_PROPERTIES_FILE = "config.properties";

    private boolean resouceSource = true;

    private final PropertyConfig properties;
    @Getter
    private final String resourceConfigDir;

    private final File configDir;

    public EvalConfig(File dir) {
        resouceSource = false;
        resourceConfigDir = null;
        properties = PropertyConfig.loadFromFile(new File(dir, CONFIG_PROPERTIES_FILE));
        configDir = dir;

    }

    public EvalConfig(String configName) {
        resouceSource = true;
        configDir = null;

        // read in all configuration for all the evaluation components:
        resourceConfigDir = "/config/" + configName + "/";

        properties = PropertyConfig.loadFromResourceFile(resourceConfigDir + CONFIG_PROPERTIES_FILE);
    }

    /**
     * Copies the configuration to another directory. Only the main config.properties file gets copied.
     *
     * @param target
     */
    public void copyConfig(Path target) {
        if (resouceSource) {
            copyResourceFile(resourceConfigDir + CONFIG_PROPERTIES_FILE, target);
        } else {
            copyFile(new File(configDir, CONFIG_PROPERTIES_FILE).toPath(), target);
        }
    }

    private void copyFile(Path source, Path target) {
        try {
            Files.copy(source, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyResourceFile(String resourceFilePath, Path target) {
        InputStream is =
                PropertyConfig.class.getResourceAsStream(resourceFilePath);
        try {
            Files.copy(is, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EvalConfig() {
        this("current");
    }

    public boolean getBoolProp(String propName) {
        return properties.getBoolProp(propName);
    }

    public int getIntProp(String propName) {
        return properties.getIntProp(propName);
    }

    public float getFloatProp(String propName) {
        return properties.getFloatProp(propName);
    }

    public ArrayFunction parseArray(String propName) {
        try {
            return FunctionParser.parseArray(getProp(propName));
        } catch (RuntimeException r) {
            throw new ConfigParseException("Error parsing Property " + propName, r);
        }
    }

    public FloatArrayFunction parseFloatArray(String propName) {
        try {
            return FunctionParser.parseFloatArray(getProp(propName));
        } catch (RuntimeException r) {
            throw new ConfigParseException("Error parsing Property " + propName, r);
        }
    }

    private String getProp(String propName) {
        return properties.getProperty(propName);
    }

    public List<MaterialCorrectionRule> parseMaterialRules() {
        return properties.getStringPropertyNames().stream().filter(n -> n.startsWith("materialRule."))
                .map(n -> parse(n.replace("materialRule.", ""), properties.getProperty(n)))
                .collect(toList());
    }

    public Pattern loadFromRelPath(String relPathName) {
        if (resouceSource) {
            String fullPath = getResourceConfigDir() + relPathName;
            return loadFromResourcePath(fullPath);
        } else {
            File patternFile = new File(configDir + "/" + relPathName);
            try (InputStream is = new FileInputStream(patternFile)) {
                return parsePattern(is);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not load pst pattern from resource file "
                        + patternFile.getAbsolutePath(), e);
            }
        }
    }

    public static Pattern loadFromResourcePath(String fullPath) {

        InputStream is = Pattern.class.getResourceAsStream(fullPath);
        if (is == null) {
            throw new IllegalArgumentException("Could not load pst pattern from resource file " + fullPath);
        }
        return parsePattern(is);
    }

    public static Pattern parsePattern(InputStream is) {
        List<Integer> values = new ArrayList<>();
        new BufferedReader(new InputStreamReader(is))
                .lines().forEach(line -> {
                    if (line.startsWith("//")) {

                    } else {
                        // parse and append the values:
                        values.addAll(Arrays.stream(line.split(";"))
                                .map(s -> s.trim())
                                .map(s -> Integer.parseInt(s))
                                .collect(Collectors.toList()));
                    }
                });

        return new Pattern(values.stream().mapToInt(i -> i.intValue()).toArray());

    }
}
