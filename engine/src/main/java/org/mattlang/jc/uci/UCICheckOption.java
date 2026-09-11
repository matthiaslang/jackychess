package org.mattlang.jc.uci;

/**
 * Defines an uci check option for this engine.
 * <p>
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
    public Boolean parse(String newValue) {
        return "true".equalsIgnoreCase(newValue);
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type check default " + getDefaultValue();
    }

}
