package org.mattlang.jc.uci;

import org.mattlang.jc.UCILogger;

/**
 * Defines an uci spin option for this engine.
 *
 * example: "option name " + OP_THINKTIME + " type spin default 15 min 5 max 600";
 */
public class UCISpinOption extends UCIOption<Integer> {

    private int min;
    private int max;

    public UCISpinOption(UCIOptions optionBundle, UCIGroup group, String name, String description, int min, int max,
            int defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        this.min = min;
        this.max = max;
        setDefaultValue(defaultValue);
        setValue(defaultValue);
        if (min > max) {
            throw new IllegalArgumentException("min > max!");
        }
        if (defaultValue < min || defaultValue > max) {
            throw new IllegalArgumentException("default not within [min,max] !");
        }
    }

    @Override
    public void parseAndSetParameter(String newValue) {
        int val = Integer.parseInt(newValue);
        if (val < min || val > max) {
            UCILogger.log(getName() + ": value not within [min,max] !");
            if (val < min) {
                setValue(min);
            } else if (val > max) {
                setValue(max);
            }

        } else {
            setValue(val);
        }
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type spin default " + getDefaultValue() + " min " + min + " max " + max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
