package org.mattlang.tuning.evaluate;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.tuning.IntIntervall;

public class MaterialValueParam extends IntegerValueParam {

    private static final IntIntervall MATERIAL_VALUE_INTERVAL = new IntIntervall(0, 2000);


    public MaterialValueParam(String name,
            ParameterizedEvaluation evaluation,
            Function<ParameterizedMaterialEvaluation, Integer> getter,
            BiConsumer<ParameterizedMaterialEvaluation, Integer> saver) {
        super(name, evaluation, new Function<ParameterizedEvaluation, Integer>() {

            @Override
            public Integer apply(ParameterizedEvaluation parameterizedEvaluation) {
                return getter.apply(parameterizedEvaluation.getMatEvaluation());
            }
        }, new BiConsumer<ParameterizedEvaluation, Integer>() {

            @Override
            public void accept(ParameterizedEvaluation parameterizedEvaluation, Integer integer) {
                    saver.accept(parameterizedEvaluation.getMatEvaluation(), integer);
            }
        },
                MATERIAL_VALUE_INTERVAL);

    }


}
