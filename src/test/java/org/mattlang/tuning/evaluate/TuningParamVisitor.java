package org.mattlang.tuning.evaluate;

import java.util.ArrayList;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.annotation.configure.EvalConfigVisitor;
import org.mattlang.jc.engine.evaluation.annotation.configure.FieldAccessStack;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.Getter;

public class TuningParamVisitor implements EvalConfigVisitor {

    private ParameterizedEvaluation evaluation;

    /**
     * all parameter groups.
     */
    @Getter
    private ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    public TuningParamVisitor(ParameterizedEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public void visitMgEgIntProperty(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {

        if (!evalConfigParam.disableTuning()) {
            groups.add(new ChangeableMgEgScoreParameterGroup(evaluation,
                    fieldAccessStack,
                    propMg,
                    propEg,
                    deriveInterval(definedInterval)));
        }
    }

    @Override
    public void visitIntProperty(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {
        if (!evalConfigParam.disableTuning()) {
            groups.add(new IntegerValueParam(qualifiedName, evaluation,
                    e -> (Integer) fieldAccessStack.get(e),
                    (e, val) -> fieldAccessStack.set(e, val),
                    deriveInterval(definedInterval)));
        }
    }

    @Override
    public void visitFloatProperty(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam) {
        if (!evalConfigParam.disableTuning()) {
            groups.add(new FloatValueParam(qualifiedName, evaluation,
                    e -> (Float) fieldAccessStack.get(e),
                    (e, val) -> fieldAccessStack.set(e, val),
                    deriveFloatInterval()));
        }
    }

    @Override
    public void visitFloatArray(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam) {
        if (!evalConfigParam.disableTuning()) {
            groups.add(new FloatArrayFunctionParameterGroup(
                    qualifiedName,
                    evaluation,
                    e -> (FloatArrayFunction) fieldAccessStack.get(e),
                    deriveFloatInterval()));
        }
    }

    @Override
    public void visitMgEgArray(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {

        if (!evalConfigParam.disableTuning()) {
            groups.add(new ArrayFunctionParameterGroup(propMg,
                    evaluation,
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).functionMg,
                    deriveInterval(definedInterval),
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).updateCombinedVals()));

            groups.add(new ArrayFunctionParameterGroup(propEg,
                    evaluation,
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).functionEg,
                    deriveInterval(definedInterval),
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).updateCombinedVals()));
        }
    }

    @Override
    public void visitMgEgPattern(String subDir, String tableCsvPathNameMg, String tableCsvPathNameEg,
            FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam, EvalValueInterval intIntervall) {
        if (!evalConfigParam.disableTuning()) {
            groups.add(new PatternParameterGroup(subDir,
                    evalConfigParam.configName() + "MG" + ".csv",
                    evalConfigParam.configName() + "EG" + ".csv",
                    true,
                    evaluation,
                    e -> (Pattern) fieldAccessStack.get(e)));
        }
    }

    @Override
    public void visitPattern(String subDir, String tableCsvPathName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval intIntervall) {
        if (!evalConfigParam.disableTuning()) {
            throw new IllegalArgumentException("Tuning of normal patterns not supported anymore! only mg/eg patterns!");
        }
    }

    @Override
    public void visitArray(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {
        // do not tune non mg/eg arrays
    }

    @Override
    public void visitArray(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval) {
        // do not tune non mg/eg arrays
    }

    private FloatIntervall deriveFloatInterval() {
        // todo not configurable via annotations at the moment..
        return new FloatIntervall(0.1f, 3f);
    }

    private IntIntervall deriveInterval(EvalValueInterval definedInterval) {
        if (definedInterval != null) {
            return new IntIntervall(definedInterval.min(), definedInterval.max());
        } else {
            // use standard interval:
            return new IntIntervall(-500, 500);
        }
    }
}
