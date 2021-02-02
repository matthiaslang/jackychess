package org.mattlang.jc.uci;

import java.util.Map;
import java.util.Objects;

/**
 * Defines an uci option for this engine.
 *
 */
public abstract class UCIOption<T> {

    private String name;

    public UCIOption(Map<String, UCIOption> optionBundle, String name) {
        this.name = Objects.requireNonNull(name);
        optionBundle.put(name, this);
    }

    public static void writeOptionsDescriptions(Map<String, UCIOption> optionBundle) {
        for (UCIOption uciOption : optionBundle.values()) {
            uciOption.writeOptionDeclaration();
        }
    }

    public static void parseOption(Map<String, UCIOption> optionBundle, String option, String value) {
        UCIOption uciOpt = optionBundle.get(option);
        if (uciOpt == null) {
            throw new IllegalArgumentException("No UCI option with name " + option + " exists!");
        }
        uciOpt.parseAndSetParameter(value);
    }

    public abstract void parseAndSetParameter(String newValue);

    public String getName() {
        return name;
    }

    public abstract void writeOptionDeclaration();

    public abstract T getValue();

    public abstract void setValue(T newValue);
}
