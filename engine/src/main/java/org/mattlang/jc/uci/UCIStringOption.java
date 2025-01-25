package org.mattlang.jc.uci;

/**
 * Defines an uci string option for this engine.
 * <p>
 * example: "option name UCI_EngineAbout type string default the text of the option";
 */
public class UCIStringOption extends UCIOption<String> {

    private String defaultValue;
    private String value;

    public UCIStringOption(UCIOptions optionBundle, UCIGroup group, String name, String description,
                           String defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        this.value = newValue;
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type string default " + defaultValue;
    }

    @Override
    public String getInternalValue() {
        return value;
    }

    @Override
    public void setValue(String newValue) {
        value = newValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
