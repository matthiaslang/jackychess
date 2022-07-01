package org.mattlang.tuning.evaluate;

import java.util.function.IntConsumer;

import org.mattlang.tuning.TuningParameter;

public class MaterialValueParam implements TuningParameter {

    private final IntConsumer saver;
    private String name;
    private int value;

    public MaterialValueParam(String name, int value, IntConsumer saver) {
        this.name = name;
        this.value = value;
        this.saver = saver;
    }

    @Override
    public void change(int i) {
        value += i;
    }

    @Override
    public String getParamDef() {
        return name + "=" + value;
    }

    public void saveValue() {
        saver.accept(value);
    }
}
