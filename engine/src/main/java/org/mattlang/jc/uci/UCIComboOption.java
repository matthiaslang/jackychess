package org.mattlang.jc.uci;

/**
 * Defines an uci combo option for this engine.
 *
 * example:    "option name Style type combo default Normal var Solid var Normal var Risky\n"
 */
public class UCIComboOption<E extends Enum> extends UCIOption<E> {

    private final Class<E> eclass;

    public UCIComboOption(UCIOptions optionBundle, UCIGroup group, String name, String description, Class<E> eclass,
            E defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        setDefaultValue(defaultValue);
        setValue(defaultValue);
        this.eclass = eclass;
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        setValue((E) Enum.valueOf(eclass, newValue));
    }

    @Override
    public String createOptionDeclaration() {
        StringBuilder b = new StringBuilder();
        for (E enumConstant : eclass.getEnumConstants()) {
            b.append(" var " + enumConstant);
        }
        String values = b.toString();
        return "option name " + getName() + " type combo default " + getDefaultValue() + values;
    }

}
