package org.mattlang.tuning.evaluate;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

public class MaterialValueParam extends IntegerValueParam {

    private final BiConsumer<ParameterizedMaterialEvaluation, Integer> saver;

    private final Function<ParameterizedMaterialEvaluation, Integer> getter;

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
        });
       this.saver=saver;
       this.getter=getter;
    }


}
