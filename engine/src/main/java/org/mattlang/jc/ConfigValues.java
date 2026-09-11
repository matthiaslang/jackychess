package org.mattlang.jc;

import lombok.Getter;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.uci.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import static org.mattlang.jc.Constants.MAX_THREADS;

/**
 * Contains all UCI Parameters as well as internal development relevant configurations.
 */
public class ConfigValues {

    private static ConfigValues configValues = new ConfigValues();

    /**
     * contains input of all uci options as raw string values. useful for internally used
     * options used during development or for tuning.
     */
    private HashMap<String, String> rawOptions = new HashMap<>();

    private ConfigValues() {
    }


    private HashSet<ConfigurationListener> configurableListeningObjects = new HashSet<>();

    public void registerConfigurableListeningObject(ConfigurationListener o) {
        configurableListeningObjects.add(o);
    }

    public static final ConfigValues getConfigValues() {
        return configValues;
    }

    public static void resetConfigValues() {
        HashSet<ConfigurationListener> registeredObjects = configValues.configurableListeningObjects;
        configValues = new ConfigValues();
        configValues.configurableListeningObjects = registeredObjects;
    }

    @Getter
    private final UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = allOptions.createGroup("Common", "Common parameter");
    public final UCIGroup variants = allOptions.createGroup("Variants", "Game Variant parameter");

    public final UCIGroup internal =
            allOptions.createInternalGroup("Internal", "Internal Test Parameter for Development");


    public final UCIGroup limits =
            allOptions.createGroup("Limits", "Parameter which limit the search or search time in some way.");

    public final UCISpinOption maxThreads = limits.createSpinOpt("Threads",
            "the maximum search threads to use for search",
            1, MAX_THREADS, 1);

    /**
     * to inform the gui that we support chess960. There is no other uci support from our side necessary, since the
     * board representation and fen parser itself is "compatible" with frc by itself.
     */
    public final UCICheckOption uciChess960 = variants.createCheckOpt("UCI_Chess960",
            "indicates support for Chess960",
            false);

    /**
     * Marker send from Gui that we are in analysis mode.
     * Currently we do not make a difference yet.
     */
    public final UCICheckOption uciAnalyseMode = variants.createCheckOpt("UCI_AnalyseMode",
            "indicates we are in Analysis Mode",
            false);

    public final UCIConstantStringOption uciAbout = variants.createConstStringOpt("UCI_EngineAbout",
            "uci engine about",
            "JackyChess by Matthias Lang, see https://github.com/matthiaslang/jackychess");

    public final UCICheckOption ponder = variants.createCheckOpt("Ponder",
            "ponder mode",
            false);

    public final UCIGroup caching =
            allOptions.createGroup("Caching", "Parameter for caching of information during search.");

    public final UCISpinOption hash = caching.createSpinOpt("Hash",
            "TT Hash Size in MB",
            1, 1024 * 32, 128);

    {
        hash.setChangeListener(newValue -> Caching.CACHING.getTtCache().checkUpdateCacheSize());
    }

    public final UCIGroup search = allOptions.createInternalGroup("Search", "Parameter that influence search.");


    public void addRawOptionVal(String option, String value) {
        rawOptions.put(option, value);

        for (ConfigurationListener configurableListeningObject : configurableListeningObjects) {
            configureOptions(configurableListeningObject);
            configurableListeningObject.configChanged();
        }
    }

    private void configureOptions(Object eval) {
        for (Field declaredField : eval.getClass().getDeclaredFields()) {
            UciConfigParam uciConfigParam = declaredField.getAnnotation(UciConfigParam.class);
            if (uciConfigParam != null) {
                String optionsVal = rawOptions.get(declaredField.getName());
                if (optionsVal != null) {
                    if (declaredField.getType() == String.class) {
                        setFieldValue(eval, declaredField, optionsVal);
                    } else if (declaredField.getType() == Integer.class || declaredField.getType() == int.class) {
                        int converted = Integer.parseInt(optionsVal);
                        setFieldValue(eval, declaredField, converted);
                    } else {
                        throw new IllegalStateException("not supported type for conf params!" + declaredField.getType().getSimpleName());
                    }
                }
            }
        }
    }

    public static void setFieldValue(Object eval, Field declaredField, Object intVal) {
        try {
            declaredField.setAccessible(true);
            declaredField.set(eval, intVal);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
