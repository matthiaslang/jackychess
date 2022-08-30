package org.mattlang.tuning.evaluate;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedAdjustmentsEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

public class AdjustmentValueParam extends IntegerValueParam {

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
        });
        this.saver = saver;
        this.getter = getter;
    }

}
