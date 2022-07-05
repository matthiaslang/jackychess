package org.mattlang.tuning.evaluate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public class PstPatternParameterGroup implements TuningParameterGroup {

    private final String tableCsvName;

    private final Function<ParameterizedPstEvaluation, Pattern> getter;

    private Pattern pattern;

    private List<TuningParameter> parameters = new ArrayList<>();

    public PstPatternParameterGroup(String tableCsvName, ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedPstEvaluation, Pattern> getter) {
        this.tableCsvName = tableCsvName;
        this.getter = getter;
        this.pattern = getter.apply(parameterizedEvaluation.getPstEvaluation()).copy();
        for (int pos = 0; pos < 64; pos++) {
            parameters.add(new PstValueParam(this, pos, pattern.getVal(pos, Color.WHITE)));
        }
    }

    @Override
    public List<TuningParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getParamDef() {
        // update the pattern with current param values:
        for (TuningParameter parameter : parameters) {
            PstValueParam pstParam = (PstValueParam) parameter;
            pattern.setVal(pstParam.getPos(), pstParam.getVal());
        }

        return tableCsvName + "\n" + pattern.toPatternStr();
    }

    public void setVal(ParameterizedEvaluation evaluation, int pos, int val) {
        getter.apply(evaluation.getPstEvaluation()).setVal(pos, val);
    }
}
