package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.evaluation.pstEval2.PstEvaluation2;

public enum EvalFunctions {

    DEFAULT {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return DefaultEvaluateFunction::new;
        }
    },

    MINIMAL_PST {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return MinimalPstEvaluation::new;
        }
    },

    PST2 {
        @Override
        public Supplier<EvaluateFunction> createSupplier() {
            return PstEvaluation2::new;
        }
    };

    public abstract Supplier<EvaluateFunction> createSupplier();
}
