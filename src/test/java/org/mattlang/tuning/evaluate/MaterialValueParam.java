package org.mattlang.tuning.evaluate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public class MaterialValueParam implements TuningParameter, TuningParameterGroup {

    private final BiConsumer<ParameterizedMaterialEvaluation, Integer> saver;

    private final Function<ParameterizedMaterialEvaluation, Integer> getter;

    private String name;
    private int value;

    public MaterialValueParam(String name,
            ParameterizedEvaluation evaluation,
            Function<ParameterizedMaterialEvaluation, Integer> getter,
            BiConsumer<ParameterizedMaterialEvaluation, Integer> saver) {
        this.name = name;
        this.saver = saver;
        this.getter = getter;
        value = getter.apply(evaluation.getMatEvaluation());
    }

    @Override
    public void change(int i) {
        value += i;
    }

    @Override
    public String getParamDef() {
        return name + "=" + value;
    }

    @Override
    public void writeParamDef(File outputDir) {

    }

    public void saveValue(ParameterizedEvaluation evaluation) {
        saver.accept(evaluation.getMatEvaluation(), value);
    }

    @Override
    public List<TuningParameter> getParameters() {
        return Arrays.asList(this);
    }
}
