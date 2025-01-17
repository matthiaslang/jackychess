package org.mattlang.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.uci.UCIGroup;
import org.mattlang.jc.uci.UCIOption;

public class SearchParameter {

    private static final Logger LOGGER = Logger.getLogger(SearchParameter.class.getSimpleName());

    private List<Impl> impls = new ArrayList<>();

    private ConfigValues config = new ConfigValues();

    public final Impl<IterativeDeepeningSearch> searchMethod = new Impl<>(this, IterativeDeepeningPVS::new);

    public void log() {
        UCILogger.log("Search Method: " + searchMethod.instance().getClass().getSimpleName()
                + " Evaluation: " + Configurator.determineEvalImplName());
        for (Map.Entry<UCIGroup, List<UCIOption>> entry : config.getAllOptions().getOptionsByGroup().entrySet()) {
            UCIGroup group = entry.getKey();
            List<UCIOption> opts = entry.getValue();
            if (group != config.common) {
                StringBuilder b = new StringBuilder();
                b.append(group.getName()).append(": ");
                b.append(opts.stream()
                        .map(o -> o.getName() + ": " + o.getValue())
                        .collect(Collectors.joining("; ")));
                UCILogger.log(b.toString());

            } else {
                for (UCIOption option : opts) {
                    UCILogger.log(option.getName() + ": " + option.getValue());
                }
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Board: " + Configurator.determineBoardImplName());
            LOGGER.info("Search Method: " + searchMethod.instance().getClass().getSimpleName());
            LOGGER.info("Evaluation: " + Configurator.determineEvalImplName());
            for (UCIOption option : config.getAllOptions().getAllOptions()) {
                LOGGER.info(option.getName() + ": " + option.getValue());
            }
        }

    }

    public void log(StringBuilder b) {
        b.append("Board: " + Configurator.determineBoardImplName());
        b.append("\n");
        b.append("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        b.append("\n");
        b.append("Evaluation: " + Configurator.determineEvalImplName());
        b.append("\n");
        for (UCIOption option : config.getAllOptions().getAllOptions()) {
            b.append(option.getName() + ": " + option.getValue());
            b.append("\n");
        }

    }

    public Map collectStatistics() {
        HashMap stats = new HashMap();
        for (Impl impl : impls) {
            impl.collectStatistics(stats);
        }
        return stats;
    }

    public <T> void register(Impl impl) {
        impls.add(impl);
    }

    public static void printStats(Map stats) {
        printStats("", stats);
    }

    public void printStats() {
        printStats(collectStatistics());
    }

    public static void printStats(String prefix, Map stats) {
        StringBuilder b = new StringBuilder();
        stats.forEach((key, value) -> {
            if (value instanceof Map) {
                printStats(prefix + "." + key.toString(), (Map) value);
            } else {
                if (b.length() > 0) {
                    b.append(", ");
                }
                b.append(key + ":" + value);
            }
        });
        if (b.length() > 0) {
            System.out.println(prefix + ": " + b.toString());
        }
    }

    public ConfigValues getConfig() {
        return config;
    }

    public SearchParameter config(Consumer<ConfigValues> cfgChange) {
        cfgChange.accept(config);
        return this;
    }

    public SearchParameter setConfig(ConfigValues config) {
        this.config = config;
        return this;
    }
}
