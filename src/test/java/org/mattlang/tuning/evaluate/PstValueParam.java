package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.tuning.TuningParameter;

public class PstValueParam implements TuningParameter {

    private final String tableCsvName;
    private final Pattern pattern;
    private final int pos;

    public PstValueParam(String tableCsvName, Pattern pattern, int pos) {
        this.tableCsvName = tableCsvName;
        this.pattern = pattern;
        this.pos = pos;
    }

    @Override
    public void change(int offset) {
        pattern.addOffsetVal(pos, offset);
    }

    @Override
    public String getParamDef() {
        return tableCsvName + "\n" + pattern.toPatternStr();
    }
}
