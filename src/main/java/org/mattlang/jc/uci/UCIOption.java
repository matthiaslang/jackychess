package org.mattlang.jc.uci;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.AppConfiguration;

import lombok.Getter;

/**
 * Defines an uci option for this engine.
 */
public abstract class UCIOption<T> {

    private static final Logger LOGGER = Logger.getLogger(UCIOption.class.getName());

    @Getter
    private UCIGroup group;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private OptionType type = OptionType.UCI;

    public UCIOption(UCIOptions optionBundle, UCIGroup group, String name, String description) {
        this.group = requireNonNull(group);
        this.name = requireNonNull(name);
        this.description = requireNonNull(description);
        optionBundle.put(name, this);
    }

    public static void writeOptionsDescriptions(UCIOptions optionBundle) {
        for (UCIOption uciOption : optionBundle.getAllOptions()) {
            if (uciOption.type == OptionType.UCI) {
                uciOption.writeOptionDeclaration();
            }
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

    public final T getValue(){

        Optional<String> optStrVal = AppConfiguration.APPCONFIG.getStringValue("opt." + getName());
        if (optStrVal.isPresent()) {
            LOGGER.log(Level.INFO, "Overrider: opt." + getName() + "=" + optStrVal.get());
            parseAndSetParameter(optStrVal.get());
        }

        return getInternalValue();
    }

    protected abstract T getInternalValue();

    public abstract void setValue(T newValue);

}
