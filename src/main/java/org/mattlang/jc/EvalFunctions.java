package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt2;
import org.mattlang.jc.engine.evaluation.taperedEval.GamePhaseEvaluation;

public enum EvalFunctions {

    DEFAULT {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return MaterialNegaMaxEvalOpt2::new;
        }
    },

    TAPERED {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return GamePhaseEvaluation::new;
        }
    };

    public abstract Supplier<EvaluateFunction> createSupplier();
}
