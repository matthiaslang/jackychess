package org.mattlang.jc.uci;

/**
 * Defines an uci combo option for this engine.
 *
 * example:    "option name Style type combo default Normal var Solid var Normal var Risky\n"
 */
public class UCIComboOption<E extends Enum> extends UCIOption<E> {

    private final Class<E> eclass;
    private E defaultValue;
    private E value;

    public UCIComboOption(UCIOptions optionBundle, UCIGroup group, String name, Class<E> eclass, E defaultValue) {
        super(optionBundle, group, name);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.eclass = eclass;
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        this.value = (E) Enum.valueOf(eclass, newValue);
    }

    @Override
    public void writeOptionDeclaration() {
        StringBuilder b = new StringBuilder();
        for (E enumConstant : eclass.getEnumConstants()) {
            b.append(" var " + enumConstant);
        }
        String values = b.toString();
        UCI.instance.putCommand(
                "option name " + getName() + " type combo default " + defaultValue + values);
    }

    @Override
    public E getValue() {
        return value;
    }

    @Override
    public void setValue(E newValue) {
        value = newValue;
    }

    public E getDefaultValue() {
        return defaultValue;
    }
}
