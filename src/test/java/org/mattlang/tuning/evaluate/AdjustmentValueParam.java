package org.mattlang.tuning.evaluate;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedAdjustmentsEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.IntIntervall;

public class AdjustmentValueParam extends IntegerValueParam {

    // interval for adjustment parameter tuning: we leave here very wide ranges (not really relevant)
    public static final IntIntervall ADJUSTMENT_VALUE_INTERVAL=new IntIntervall(-2000, +2000);

    public AdjustmentValueParam(String name,
            ParameterizedEvaluation evaluation,
            Function<ParameterizedAdjustmentsEvaluation, Integer> getter,
            BiConsumer<ParameterizedAdjustmentsEvaluation, Integer> saver) {
        super(name, evaluation, new Function<ParameterizedEvaluation, Integer>() {

            @Override
            public Integer apply(ParameterizedEvaluation parameterizedEvaluation) {
                return getter.apply(parameterizedEvaluation.getAdjustments());
            }
        }, new BiConsumer<ParameterizedEvaluation, Integer>() {

            @Override
            public void accept(ParameterizedEvaluation parameterizedEvaluation, Integer integer) {
                saver.accept(parameterizedEvaluation.getAdjustments(), integer);
            }
        }, ADJUSTMENT_VALUE_INTERVAL);
    }

}
