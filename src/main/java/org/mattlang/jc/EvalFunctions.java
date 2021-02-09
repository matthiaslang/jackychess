package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEvalOpt2;
import org.mattlang.jc.engine.evaluation.taperedEval.GamePhaseEvaluation;

public enum EvalFunctions {

    SIMPLE {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return MaterialNegaMaxEvalOpt::new;
        }
    },
    SIMPLE_AND_PAWN_STRUCTURE {
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
