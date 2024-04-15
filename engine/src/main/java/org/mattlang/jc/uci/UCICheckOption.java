package org.mattlang.jc.uci;

/**
 * Defines an uci check option for this engine.
 *
 * example:   "option name Nullmove type check default true\n"
 */
public class UCICheckOption extends UCIOption<Boolean> {

    private boolean defaultValue;
    private boolean value;

    public UCICheckOption(UCIOptions optionBundle, UCIGroup group, String name, String description,
            boolean defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        this.value = "true".equalsIgnoreCase(newValue);
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type check default " + defaultValue;
    }

    @Override
    public Boolean getInternalValue() {
        return value;
    }

    @Override
    public void setValue(Boolean newValue) {
        value = newValue;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }
}
