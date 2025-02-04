package org.mattlang.jc.uci;

/**
 * Defines an uci check option for this engine.
 *
 * example:   "option name Nullmove type check default true\n"
 */
public class UCICheckOption extends UCIOption<Boolean> {

    public UCICheckOption(UCIOptions optionBundle, UCIGroup group, String name, String description,
            boolean defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        setDefaultValue(defaultValue);
        setValue(defaultValue);
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        setValue("true".equalsIgnoreCase(newValue));
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type check default " + getDefaultValue();
    }

}
