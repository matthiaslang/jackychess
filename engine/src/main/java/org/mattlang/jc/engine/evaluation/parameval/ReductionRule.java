package org.mattlang.jc.engine.evaluation.parameval;

public enum ReductionRule {

    HALFRESULT("reduce half"),
    ZERO("reduce all");

    private final String txt;

    ReductionRule(String txt) {
        this.txt = txt;
    }

    public static ReductionRule findByTxt(String str) {
        for (ReductionRule value : ReductionRule.values()) {
            if (value.txt.equalsIgnoreCase(str.trim())) {
                return value;
            }
        }
        throw new ConfigParseException("Could not parse ReductionRule: " + str);
    }

    public int adjust(int result) {
        if (this == HALFRESULT) {
            return result / 2;
        } else {
            return 0;
        }
    }
}
