package org.mattlang.jc.uci;

/**
 * Constant string options, which are only used as marker, but are not changeable options.
 */
public class UCIConstantStringOption extends UCIStringOption {

    public UCIConstantStringOption(UCIOptions optionBundle, UCIGroup group, String name, String description, String defaultValue, OptionType type) {
        super(optionBundle, group, name, description, defaultValue, type);
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        // dont do anything, since it is "constant"
    }
}
