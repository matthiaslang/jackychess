package org.mattlang.tuning.evaluate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public class IntegerValueParam implements TuningParameter, TuningParameterGroup {

    private final BiConsumer<ParameterizedEvaluation, Integer> saver;

    private final Function<ParameterizedEvaluation, Integer> getter;

    private String name;
    private int value;

    public IntegerValueParam(String name,
            ParameterizedEvaluation evaluation,
            Function<ParameterizedEvaluation, Integer> getter,
            BiConsumer<ParameterizedEvaluation, Integer> saver) {
        this.name = name;
        this.saver = saver;
        this.getter = getter;
        value = getter.apply(evaluation);
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
        exchangeParam(new File(outputDir, "config.properties"), name, value);
    }

    private void exchangeParam(File file, String name, int value) {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(name + "=")) {
                    fileContent.set(i, name + "=" + value);
                    break;
                }
            }

            Files.write(file.toPath(), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveValue(ParameterizedEvaluation evaluation) {
        saver.accept(evaluation, value);
    }

    @Override
    public List<TuningParameter> getParameters() {
        return Arrays.asList(this);
    }
}
