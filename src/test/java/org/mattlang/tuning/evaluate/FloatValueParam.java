package org.mattlang.tuning.evaluate;

import lombok.Getter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FloatValueParam extends AbstractTuningParameter implements TuningParameterGroup {

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

    @Override
    public int getValue() {
        return (int) (value * 10);
    }

    @Override
    public void setValue(int val) {
        value = calcChange(val);
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

    @Override
    public String getDescr() {
        return name;
    }

    @Override
    public void resetValue() {
        value=1;
    }
}
