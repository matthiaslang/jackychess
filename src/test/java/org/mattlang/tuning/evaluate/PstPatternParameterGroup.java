package org.mattlang.tuning.evaluate;


import java.util.function.Consumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;

public class PstPatternParameterGroup extends PatternParameterGroup {

    public PstPatternParameterGroup(String tableCsvName,
            boolean mirrored,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedPstEvaluation, Pattern> getter,
            Consumer<ParameterizedEvaluation> afterUpdateCallback) {

        super("pst", tableCsvName, mirrored,
                parameterizedEvaluation,
                parameterizedEvaluation1 -> getter.apply(parameterizedEvaluation1.getPstEvaluation()),
                afterUpdateCallback);
    }

}
