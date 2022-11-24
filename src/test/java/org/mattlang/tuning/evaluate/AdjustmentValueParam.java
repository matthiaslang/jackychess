package org.mattlang.tuning.evaluate;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedAdjustmentsEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.Intervall;

public class AdjustmentValueParam extends IntegerValueParam {

    // interval for adjustment parameter tuning: we leave here very wide ranges (not really relevant)
    private static final Intervall ADJUSTMENT_VALUE_INTERVAL=new Intervall(-2000, +2000);
    private final BiConsumer<ParameterizedAdjustmentsEvaluation, Integer> saver;

    private final Function<ParameterizedAdjustmentsEvaluation, Integer> getter;

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
        this.saver = saver;
        this.getter = getter;
    }

}
