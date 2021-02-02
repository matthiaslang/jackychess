package org.mattlang.jc;

import java.util.HashMap;
import java.util.Map;

import org.mattlang.jc.uci.UCIOption;
import org.mattlang.jc.uci.UCISpinOption;
import org.mattlang.jc.uci.UCITimeoutOption;

public class ConfigValues {

    private Map<String, UCIOption> allOptions = new HashMap<>();

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions);
    public final UCISpinOption maxQuiescence = new UCISpinOption(allOptions, "quiescence", 0, 6, 0);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, "maxdepth", 5, 30, 15);

    public Map<String, UCIOption> getAllOptions() {
        return allOptions;
    }
}
