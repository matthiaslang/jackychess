package org.mattlang.tuning.evaluate;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigPrefix;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.annotation.configure.ConfigFilter;
import org.mattlang.jc.engine.evaluation.annotation.configure.FieldAccessStack;
import org.mattlang.jc.engine.evaluation.annotation.configure.PrefixStack;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.extern.java.Log;

@Log
public class TuningParamConfigurator {

    private ParameterizedEvaluation evaluation;

    /**
     * all parameter groups.
     */
    private ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    public TuningParamConfigurator() {

    }

    public ArrayList<TuningParameterGroup> configure(ParameterizedEvaluation eval, ConfigFilter configFilter) {
        groups.clear();
        this.evaluation = eval;

        final EvalConfigurable configurableTest = eval.getClass().getAnnotation(EvalConfigurable.class);
        if (configurableTest == null) {
            throw new IllegalArgumentException(eval.getClass().getSimpleName() + " is not eval configurable!");
        }
        EvalValueInterval interval = eval.getClass().getAnnotation(EvalValueInterval.class);

        configureIntern(eval, new PrefixStack(configurableTest.prefix()), new FieldAccessStack(), interval,
                configFilter);

        return groups;
    }

    private void configureIntern(Object eval, PrefixStack outerPrefixStack, FieldAccessStack fieldAccessStack,
            EvalValueInterval interval,
            ConfigFilter configFilter) {

        for (Field declaredField : eval.getClass().getDeclaredFields()) {
            EvalValueInterval intervalOfField = declaredField.getAnnotation(EvalValueInterval.class);
            EvalValueInterval effectiveInterval = intervalOfField != null ? intervalOfField : interval;

            PrefixStack prefixStackToUse = outerPrefixStack;
            EvalConfigPrefix innerPrefixStack = declaredField.getAnnotation(EvalConfigPrefix.class);
            if (innerPrefixStack != null) {
                prefixStackToUse = prefixStackToUse.with(innerPrefixStack.prefix());
            }

            FieldAccessStack nestedAccessStack = fieldAccessStack.with(declaredField);

            EvalConfigParam evalConfigParam = declaredField.getAnnotation(EvalConfigParam.class);
            if (evalConfigParam != null && !evalConfigParam.disableTuning()) {

                String fullQualifiedConfigName = prefixStackToUse.getQualifiedName(evalConfigParam);
                if (configFilter.filter(fullQualifiedConfigName)) {
                    configureField(eval, declaredField, prefixStackToUse, nestedAccessStack, evalConfigParam,
                            effectiveInterval);
                }
            }
            EvalConfigurable configurable = declaredField.getType().getAnnotation(EvalConfigurable.class);
            if (configurable != null) {
                Object fieldValue = getFieldValue(eval, declaredField);
                if (fieldValue != null) {

                    configureIntern(fieldValue, prefixStackToUse.with(configurable.prefix()), nestedAccessStack,
                            effectiveInterval, configFilter);
                }
            }
        }
    }

    private void configureField(Object eval, Field declaredField, PrefixStack prefixes,
            FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval) {
        log.fine("Configuring " + eval.getClass().getSimpleName() + "." + declaredField.getName() + " with "
                + evalConfigParam);
        if (declaredField.getType() == Integer.class || declaredField.getType() == int.class) {
            if (evalConfigParam.mgEgCombined()) {
                String propMg = prefixes.getQualifiedName(evalConfigParam) + "MG";
                String propEg = prefixes.getQualifiedName(evalConfigParam) + "EG";

                groups.add(new ChangeableMgEgScoreParameterGroup(evaluation,
                        fieldAccessStack,
                        propMg,
                        propEg,
                        deriveInterval(definedInterval)));
            } else {
                groups.add(new IntegerValueParam(prefixes.getQualifiedName(evalConfigParam), evaluation,
                        e -> (Integer) fieldAccessStack.get(e),
                        (e, val) -> fieldAccessStack.set(e, val),
                        deriveInterval(definedInterval)));
            }
        } else if (declaredField.getType() == Float.class || declaredField.getType() == float.class) {
            if (evalConfigParam.mgEgCombined()) {
                throw new IllegalArgumentException("Float values can not be configured as mg eg combined!");
            } else {
                groups.add(new FloatValueParam(prefixes.getQualifiedName(evalConfigParam), evaluation,
                        e -> (Float) fieldAccessStack.get(e),
                        (e, val) -> fieldAccessStack.set(e, val),
                        deriveFloatInterval()));
            }
        } else if (declaredField.getType() == FloatArrayFunction.class) {
            if (evalConfigParam.mgEgCombined()) {
                throw new IllegalArgumentException("Float array values can not be configured as mg eg combined!");
            } else {
                groups.add(new FloatArrayFunctionParameterGroup(
                        prefixes.getQualifiedName(evalConfigParam),
                        evaluation,
                        e -> (FloatArrayFunction) fieldAccessStack.get(e),
                        deriveFloatInterval()));
            }
        } else if (declaredField.getType() == ArrayFunction.class) {
            // do not do anything for tuning...
        } else if (declaredField.getType() == MgEgArrayFunction.class) {

            String propMg = prefixes.getQualifiedName(evalConfigParam) + "MG";
            String propEg = prefixes.getQualifiedName(evalConfigParam) + "EG";

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

        } else if (declaredField.getType() == Pattern.class) {
            if (evalConfigParam.mgEgCombined()) {

                String subDir = prefixes.with(evalConfigParam.prefix()).getQualifiedPathName();
                groups.add(new PatternParameterGroup(subDir,
                        evalConfigParam.configName() + "MG" + ".csv",
                        evalConfigParam.configName() + "EG" + ".csv",
                        true,
                        evaluation,
                        e -> (Pattern) fieldAccessStack.get(e)));

            } else {
                throw new IllegalArgumentException("not supported anymore! only mg/eg patterns!");
            }
        }
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

    public static void setFieldValue(Object eval, Field declaredField, Object intVal) {
        try {
            declaredField.setAccessible(true);
            declaredField.set(eval, intVal);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object eval, Field declaredField) {
        try {
            declaredField.setAccessible(true);
            return declaredField.get(eval);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
