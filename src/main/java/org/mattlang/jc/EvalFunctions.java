package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

import lombok.Getter;

public enum EvalFunctions {

    DEFAULT(DefaultEvaluateFunction::new),

    MINIMAL_PST(MinimalPstEvaluation::new),

    PARAMETERIZED(ParameterizedEvaluation::new);

    @Getter
    private final Supplier<EvaluateFunction> supplier;

    EvalFunctions(Supplier<EvaluateFunction> supplier) {
        this.supplier = supplier;
    }

}
