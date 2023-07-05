package org.mattlang.tuning.evaluate;

import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;

public class PstPatternParameterGroup extends PatternParameterGroup {

    public PstPatternParameterGroup(String tableCsvNameMg, String tableCsvNameEg,
            boolean mirrored,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedPstEvaluation, Pattern> getter) {

        super("pst", tableCsvNameMg, tableCsvNameEg, mirrored,
                parameterizedEvaluation,
                parameterizedEvaluation1 -> getter.apply(parameterizedEvaluation1.getPstEvaluation()));
    }

}
