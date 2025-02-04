package org.mattlang.jc.uci;

/**
 * Defines an uci string option for this engine.
 * <p>
 * example: "option name UCI_EngineAbout type string default the text of the option";
 */
public class UCIStringOption extends UCIOption<String> {

    public UCIStringOption(UCIOptions optionBundle, UCIGroup group, String name, String description,
            String defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        setDefaultValue(defaultValue);
        setValue(defaultValue);
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        setValue(newValue);
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type string default " + getDefaultValue();
    }

}
