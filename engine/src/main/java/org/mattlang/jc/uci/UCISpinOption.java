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
    private int defaultValue;
    private int value;

    public UCISpinOption(UCIOptions optionBundle, UCIGroup group, String name, String description, int min, int max,
            int defaultValue, OptionType type) {
        super(optionBundle, group, name, description, type);
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
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
            if (value < min){
                value = min;
            } else if (value > max){
                value = max;
            }

        } else {
            this.value = val;
        }
    }

    @Override
    public String createOptionDeclaration() {
        return "option name " + getName() + " type spin default " + defaultValue + " min " + min + " max " + max;
    }

    @Override
    public Integer getInternalValue() {
        return value;
    }

    @Override
    public void setValue(Integer newValue) {
        value = newValue;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
}
