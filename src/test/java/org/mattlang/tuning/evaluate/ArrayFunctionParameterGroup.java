package org.mattlang.tuning.evaluate;

import static org.mattlang.tuning.evaluate.ParamUtils.exchangeParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.Getter;

public class ArrayFunctionParameterGroup implements TuningParameterGroup {

    @Getter
    private final String propertyName;

    private final Function<ParameterizedEvaluation, ArrayFunction> getter;

    private ArrayFunction function;

    /**
     * individual parameter for each index of the function.
     */
    private List<TuningParameter> parameters = new ArrayList<>();

    public ArrayFunctionParameterGroup(String propertyName,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedEvaluation, ArrayFunction> getter, IntIntervall intervall) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.function = getter.apply(parameterizedEvaluation).copy();

        for (int index = 0; index < this.function.getSize(); index++) {
            parameters.add(new ArrayFunctionParam(this, index, function.calc(index), intervall));
        }

    }

    private ArrayFunctionParameterGroup(ArrayFunctionParameterGroup orig) {
        this.propertyName = orig.propertyName;
        this.getter = orig.getter;
        this.function = orig.function.copy();
        for (TuningParameter parameter : orig.parameters) {
            this.parameters.add(((ArrayFunctionParam) parameter).copyParam(this));
        }
    }

    @Override
    public List<TuningParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getParamDef() {
        return propertyName + " = " + createParamStr();
    }

    public String createParamStr() {
        // update the pattern with current param values:
        for (TuningParameter parameter : parameters) {
            ArrayFunctionParam param = (ArrayFunctionParam) parameter;
            function.setVal(param.getIndex(), param.getVal());

        }
        return function.convertDataToString();
    }

    @Override
    public void writeParamDef(File outputDir) {
        exchangeParam(new File(outputDir, "config.properties"), propertyName, createParamStr());
    }

    @Override
    public TuningParameterGroup copy() {
        return new ArrayFunctionParameterGroup(this);
    }

    public void setVal(ParameterizedEvaluation evaluation, int pos, int val) {
        getter.apply(evaluation).setVal(pos, val);
    }

}
