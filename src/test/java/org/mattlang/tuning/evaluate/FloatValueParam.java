package org.mattlang.tuning.evaluate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.Getter;

public class FloatValueParam implements TuningParameter, TuningParameterGroup {

    private final BiConsumer<ParameterizedEvaluation, Float> saver;

    private final Function<ParameterizedEvaluation, Float> getter;
    @Getter
    private final FloatIntervall intervall;

    private String name;
    private float value;

    public FloatValueParam(String name,
            ParameterizedEvaluation evaluation,
            Function<ParameterizedEvaluation, Float> getter,
            BiConsumer<ParameterizedEvaluation, Float> saver,
            FloatIntervall intervall) {
        this.name = name;
        this.saver = saver;
        this.getter = getter;
        this.intervall = intervall;

        value = getter.apply(evaluation);
    }

    @Override
    public void change(int i) {
        value += calcChange(i);
    }

    public static float calcChange(int i) {
        return i * 0.1f;
    }

    @Override
    public String getParamDef() {
        return name + "=" + value;
    }

    @Override
    public void writeParamDef(File outputDir) {
        ParamUtils.exchangeParam(new File(outputDir, "config.properties"), name, value);
    }

    public void saveValue(ParameterizedEvaluation evaluation) {
        saver.accept(evaluation, value);
    }

    @Override
    public List<TuningParameter> getParameters() {
        return Arrays.asList(this);
    }

    @Override
    public boolean isChangePossible(int step) {
        return intervall.isInIntervall(value + calcChange(step));
    }
}
