package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

import lombok.Getter;

public enum EvalFunctions {

    PARAMETERIZED(ParameterizedEvaluation::new);

    @Getter
    private final Supplier<EvaluateFunction> supplier;

    EvalFunctions(Supplier<EvaluateFunction> supplier) {
        this.supplier = supplier;
    }

}
