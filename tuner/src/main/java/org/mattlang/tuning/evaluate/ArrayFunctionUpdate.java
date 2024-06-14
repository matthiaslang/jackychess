package org.mattlang.tuning.evaluate;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

public interface ArrayFunctionUpdate {

    void update(ParameterizedEvaluation evaluation, int pos, int value);
}
