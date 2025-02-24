package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.FigureConstants;
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
    private final String configName;

    private final PropertyConfig properties;
    @Getter
    private final String configDir;
    @Getter
    private final String configFile;

    public EvalConfig(String configName) {
        this.configName = configName;

        // read in all configuration for all the evaluation components:
        configDir = "/config/" + configName + "/";
        configFile = configDir + CONFIG_PROPERTIES_FILE;

        properties = PropertyConfig.loadFromResourceFile(configFile);
    }

    /**
     * Copies the configuration to another directory.
     * @param target
     */
    public void copyConfig(Path target) {
        copyResourceFile(configFile, target);

        copyAllSubFiles(configDir, "pst", target);
        copyAllSubFiles(configDir, "pawn", target);
        copyAllSubFiles(configDir, "king", target);
    }

    private void copyAllSubFiles(String configDir, String subDirPath, Path target) {
        String subDir = configDir + "/" + subDirPath;
        Path targetPath = target.getParent().resolve(subDirPath);
        targetPath.toFile().mkdirs();
        for (String resourceFile : getResourceFiles(subDir)) {
            String fullSubDir = subDir + "/" + resourceFile;
            Path fullTargetPath = targetPath.resolve(resourceFile);
            copyResourceFile(fullSubDir, fullTargetPath);
        }
    }

    public void copyResourceFile(String resourceFilePath, Path target) {
        InputStream is =
                PropertyConfig.class.getResourceAsStream(resourceFilePath);
        try {
            Files.copy(is, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lists all resource files within a resource directory.
     *
     * @param path
     * @return
     * @throws IOException
     */
    private List<String> getResourceFiles(String path) {
        List<String> filenames = new ArrayList<>();
        try (InputStream in = PropertyConfig.class.getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filenames;
    }

    public EvalConfig() {
        this("current");
    }

    public int getPosIntProp(String propName) {
        int val = getIntProp(propName);
        if (val < 0) {
            throw new ConfigParseException("Value " + propName + " must not be negative!");
        }
        return val;
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

    /**
     * Parses an array which is used to index by figure code (1-6). It must therefore contain exactly 7 values
     * (first is emtpy, unused, which symbols an empty figure; index 1-6 for the figure types).
     *
     * @param configName
     * @return
     */
    public ArrayFunction parseFigureIndexedArray(String configName) {
        ArrayFunction function = parseArray(configName);
        if (function.getSize() != FigureConstants.FT_ALL) {
            throw new ConfigParseException("Error parsing figure indexed array " + configName
                    + "! It must have exactly 7 values (0, followed by 6 values for each figure type)!");
        }
        // first entry is unused: we test therefore strictly to have a value of 0:
        if (function.calc(0) != 0) {
            throw new ConfigParseException("Error parsing figure indexed array " + configName + "! Index 0 is not 0!");
        }

        return function;
    }
}
