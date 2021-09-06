package org.mattlang.jc.uci;

import static java.util.Objects.requireNonNull;

import lombok.Getter;

/**
 * Defines an uci option for this engine.
 */
public abstract class UCIOption<T> {

    @Getter
    private UCIGroup group;

    @Getter
    private String name;

    @Getter
    private String description;

    public UCIOption(UCIOptions optionBundle, UCIGroup group, String name, String description) {
        this.group = requireNonNull(group);
        this.name = requireNonNull(name);
        this.description = requireNonNull(description);
        optionBundle.put(name, this);
    }

    public static void writeOptionsDescriptions(UCIOptions optionBundle) {
        for (UCIOption uciOption : optionBundle.getAllOptions()) {
            uciOption.writeOptionDeclaration();
        }
    }

    public static void parseOption(UCIOptions optionBundle, String option, String value) {
        UCIOption uciOpt = optionBundle.find(option);
        if (uciOpt == null) {
            throw new IllegalArgumentException("No UCI option with name " + option + " exists!");
        }
        uciOpt.parseAndSetParameter(value);
    }

    public abstract void parseAndSetParameter(String newValue);

    public void writeOptionDeclaration() {
        UCI.instance.putCommand(createOptionDeclaration());
    }

    public abstract String createOptionDeclaration();

    public abstract T getValue();

    public abstract void setValue(T newValue);
}
