package org.mattlang.tuning.evaluate;

import lombok.Getter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.AbstractTuningParameter;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class IntegerValueParam extends AbstractTuningParameter implements TuningParameterGroup {

    private final BiConsumer<ParameterizedEvaluation, Integer> saver;

    private final Function<ParameterizedEvaluation, Integer> getter;

    @Getter
    private final IntIntervall intervall;

    private String name;
    private int value;

    public IntegerValueParam(String name,
                             ParameterizedEvaluation evaluation,
                             Function<ParameterizedEvaluation, Integer> getter,
                             BiConsumer<ParameterizedEvaluation, Integer> saver,
                             IntIntervall intervall) {
        this.name = name;
        this.saver = saver;
        this.getter = getter;
        this.intervall = intervall;

        value = getter.apply(evaluation);
    }

    @Override
    public void change(int i) {
        value += i;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int val) {
        value = val;
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
        return intervall.isInIntervall(value + step);
    }

    @Override
    public String getDescr() {
        return name;
    }
}
