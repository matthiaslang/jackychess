package org.mattlang.jc.uci;

import java.util.Map;

/**
 * Defines an uci check option for this engine.
 *
 * example:   "option name Nullmove type check default true\n"
 */
public class UCICheckOption extends UCIOption<Boolean> {

    private boolean defaultValue;
    private boolean value;

    public UCICheckOption(Map<String, UCIOption> optionBundle, String name, boolean defaultValue) {
        super(optionBundle, name);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        this.value = "true".equalsIgnoreCase(newValue);
    }

    @Override
    public void writeOptionDeclaration() {
        UCI.instance.putCommand(
                "option name " + getName() + " type check default " + defaultValue);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean newValue) {
        value = newValue;
    }

}
