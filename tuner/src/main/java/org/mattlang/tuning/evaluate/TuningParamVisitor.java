package org.mattlang.tuning.evaluate;

import java.util.ArrayList;

import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.annotation.configure.*;
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
    public void visitMgEgIntProperty(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {

        if (!configDefinition.getConfigDefinition().isDisableTuning()) {
            groups.add(new ChangeableMgEgScoreParameterGroup(evaluation,
                    fieldAccessStack,
                    configDefinition.getFullQualifiedPropNameMg(),
                    configDefinition.getFullQualifiedPropNameEg(),
                    deriveInterval(definedInterval)));
        }
    }

    @Override
    public void visitIntProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        if (!configDefinition.isDisableTuning()) {
            groups.add(new IntegerValueParam(configDefinition.getFullQualifiedConfigName(), evaluation,
                    e -> (Integer) fieldAccessStack.get(e),
                    (e, val) -> fieldAccessStack.set(e, val),
                    deriveInterval(definedInterval)));
        }
    }

    @Override
    public void visitFloatProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack) {
        if (!configDefinition.isDisableTuning()) {
            groups.add(new FloatValueParam(configDefinition.getFullQualifiedConfigName(), evaluation,
                    e -> (Float) fieldAccessStack.get(e),
                    (e, val) -> fieldAccessStack.set(e, val),
                    deriveFloatInterval()));
        }
    }

    @Override
    public void visitFloatArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack) {
        if (!configDefinition.isDisableTuning()) {
            groups.add(new FloatArrayFunctionParameterGroup(
                    configDefinition.getFullQualifiedConfigName(),
                    evaluation,
                    e -> (FloatArrayFunction) fieldAccessStack.get(e),
                    deriveFloatInterval()));
        }
    }

    @Override
    public void visitMgEgArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall) {

        if (!configDefinition.getConfigDefinition().isDisableTuning()) {
            groups.add(new ArrayFunctionParameterGroup(configDefinition.getFullQualifiedPropNameMg(),
                    evaluation,
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).functionMg,
                    deriveInterval(intIntervall),
                    (e, pos, value) -> ((MgEgArrayFunction) fieldAccessStack.get(e)).updateCombinedValue(pos)));

            groups.add(new ArrayFunctionParameterGroup(configDefinition.getFullQualifiedPropNameEg(),
                    evaluation,
                    e -> ((MgEgArrayFunction) fieldAccessStack.get(e)).functionEg,
                    deriveInterval(intIntervall),
                    (e, pos, value) -> ((MgEgArrayFunction) fieldAccessStack.get(e)).updateCombinedValue(pos)));
        }
    }

    @Override
    public void visitMgEgPattern(PatternConfigDefinition patternConfigDefinition,
            FieldAccessStack fieldAccessStack, EvalValueInterval intIntervall) {
        if (!patternConfigDefinition.getMgEgConfigDefinition().getConfigDefinition().isDisableTuning()) {
            groups.add(new PatternParameterGroup(patternConfigDefinition.getSubDir(),
                    patternConfigDefinition.getCsvTableNameMG(),
                    patternConfigDefinition.getCsvTableNameEG(),
                    true,
                    evaluation,
                    e -> (Pattern) fieldAccessStack.get(e)));
        }
    }

    @Override
    public void visitPattern(PatternConfigDefinition patternConfigDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall) {
        if (!patternConfigDefinition.getMgEgConfigDefinition().getConfigDefinition().isDisableTuning()) {
            throw new IllegalArgumentException("Tuning of normal patterns not supported anymore! only mg/eg patterns!");
        }
    }

    @Override
    public void visitArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        // do not tune non mg/eg arrays
    }

    @Override
    public void visitArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
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
