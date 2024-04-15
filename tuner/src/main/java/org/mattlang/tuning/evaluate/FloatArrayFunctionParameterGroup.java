package org.mattlang.tuning.evaluate;

import static org.mattlang.tuning.evaluate.ParamUtils.exchangeParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.Getter;

public class FloatArrayFunctionParameterGroup implements TuningParameterGroup {

    @Getter
    private final String propertyName;

    private final Function<ParameterizedEvaluation, FloatArrayFunction> getter;

    private FloatArrayFunction function;

    /**
     * individual parameter for each index of the function.
     */
    private List<TuningParameter> parameters = new ArrayList<>();

    public FloatArrayFunctionParameterGroup(String propertyName,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedEvaluation, FloatArrayFunction> getter, FloatIntervall intervall) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.function = getter.apply(parameterizedEvaluation).copy();

        for (int index = 0; index < this.function.getSize(); index++) {
            parameters.add(new FloatArrayFunctionParam(this, index, function.calc(index), intervall));
        }

    }

    private FloatArrayFunctionParameterGroup(FloatArrayFunctionParameterGroup orig) {
        this.propertyName = orig.propertyName;
        this.getter = orig.getter;
        this.function = orig.function.copy();
        for (TuningParameter parameter : orig.parameters) {
            this.parameters.add(((FloatArrayFunctionParam) parameter).copyParam(this));
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
            FloatArrayFunctionParam param = (FloatArrayFunctionParam) parameter;
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
        return new FloatArrayFunctionParameterGroup(this);
    }

    public void setVal(ParameterizedEvaluation evaluation, int pos, float val) {
        getter.apply(evaluation).setVal(pos, val);
    }

}
