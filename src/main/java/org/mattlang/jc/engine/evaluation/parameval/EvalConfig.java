package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.parse;

import java.util.List;

import org.mattlang.jc.Factory;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;
import org.mattlang.jc.engine.evaluation.parameval.functions.FunctionParser;
import org.mattlang.jc.util.PropertyConfig;

import lombok.Getter;

/**
 * Bundles configuration for parameterized evaluation functions.
 */
public class EvalConfig {

    private final String configName;

    private final PropertyConfig properties;
    @Getter
    private final String configDir;

    public EvalConfig() {
        configName = Factory.getDefaults().getConfig().evaluateParamSet.getValue().name().toLowerCase();

        // read in all configuration for all the evaluation components:
        configDir = "/config/" + configName + "/";

        properties = PropertyConfig.loadFromResourceFile(configDir + "config.properties");
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

    public Function parseFunction(String propName) {
        try {
            return FunctionParser.parseFunction(getProp(propName));
        } catch (RuntimeException r) {
            throw new ConfigParseException("Error parsing Property " + propName, r);
        }
    }

    public ArrayFunction parseArray(String propName) {
        try {
            return FunctionParser.parseArray(getProp(propName));
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

}
