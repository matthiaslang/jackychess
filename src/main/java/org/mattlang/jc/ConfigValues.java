package org.mattlang.jc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.uci.*;

public class ConfigValues {

    private Map<String, UCIOption> allOptions = new LinkedHashMap<>();

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions);
    public final UCISpinOption maxQuiescence = new UCISpinOption(allOptions, "quiescence", 0, 6, 0);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, "maxdepth", 5, 30, 15);

    public final UCICheckOption activatePvsSearch = new UCICheckOption(allOptions, "activatePvsSearch", false);
    public final UCICheckOption useTTCache = new UCICheckOption(allOptions, "useTTCache", false);

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm = new UCIComboOption(allOptions, "searchalg", SearchAlgorithms.class, SearchAlgorithms.ALPHA_BETA);

    public Map<String, UCIOption> getAllOptions() {
        return allOptions;
    }
}
