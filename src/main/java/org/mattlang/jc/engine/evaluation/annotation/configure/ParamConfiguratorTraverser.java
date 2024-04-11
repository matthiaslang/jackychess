package org.mattlang.jc.engine.evaluation.annotation.configure;

import static org.mattlang.jc.engine.evaluation.annotation.configure.FieldAccessStack.getFieldValue;

import java.lang.reflect.Field;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigPrefix;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.extern.java.Log;

@Log
public class ParamConfiguratorTraverser {

    private ParameterizedEvaluation evaluation;

    private EvalConfigVisitor visitor;

    public ParamConfiguratorTraverser(EvalConfigVisitor visitor) {
        this.visitor = visitor;
    }

    public void traverse(ParameterizedEvaluation eval, ConfigFilter configFilter) {
        this.evaluation = eval;

        final EvalConfigurable configurableTest = eval.getClass().getAnnotation(EvalConfigurable.class);
        if (configurableTest == null) {
            throw new IllegalArgumentException(eval.getClass().getSimpleName() + " is not eval configurable!");
        }
        EvalValueInterval interval = eval.getClass().getAnnotation(EvalValueInterval.class);

        configureIntern(eval, new PrefixStack(configurableTest.prefix()), new FieldAccessStack(), interval,
                configFilter);

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
            if (evalConfigParam != null) {

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

        ConfigDefinition configDefinition = new ConfigDefinition(prefixes, evalConfigParam, declaredField);

        log.fine("Configuring " + eval.getClass().getSimpleName() + "." + declaredField.getName()
                + ", config key: " + configDefinition.getFullQualifiedConfigName() + " annotation: " + evalConfigParam);
        if (declaredField.getType() == Integer.class || declaredField.getType() == int.class) {
            if (evalConfigParam.mgEgCombined()) {

                visitor.visitMgEgIntProperty(new MgEgConfigDefinition(configDefinition), fieldAccessStack,
                        definedInterval);
            } else {
                visitor.visitIntProperty(configDefinition, fieldAccessStack, definedInterval);
            }
        } else if (declaredField.getType() == Float.class || declaredField.getType() == float.class) {
            if (evalConfigParam.mgEgCombined()) {
                throw new IllegalArgumentException("Float values can not be configured as mg eg combined!");
            } else {
                visitor.visitFloatProperty(configDefinition, fieldAccessStack);
            }
        } else if (declaredField.getType() == FloatArrayFunction.class) {
            if (evalConfigParam.mgEgCombined()) {
                throw new IllegalArgumentException("Float array values can not be configured as mg eg combined!");
            } else {
                visitor.visitFloatArray(configDefinition, fieldAccessStack);
            }
        } else if (declaredField.getType() == ArrayFunction.class) {
            if (evalConfigParam.mgEgCombined()) {
                visitor.visitArray(new MgEgConfigDefinition(configDefinition), fieldAccessStack, definedInterval);
            } else {
                visitor.visitArray(configDefinition, fieldAccessStack, definedInterval);
            }
        } else if (declaredField.getType() == MgEgArrayFunction.class) {
            visitor.visitMgEgArray(new MgEgConfigDefinition(configDefinition), fieldAccessStack, definedInterval);
        } else if (declaredField.getType() == Pattern.class) {
            PatternConfigDefinition patternConfigDefinition =
                    new PatternConfigDefinition(new MgEgConfigDefinition(configDefinition), prefixes);

            if (evalConfigParam.mgEgCombined()) {
                visitor.visitMgEgPattern(patternConfigDefinition, fieldAccessStack, definedInterval);
            } else {
                visitor.visitPattern(patternConfigDefinition, fieldAccessStack, definedInterval);
            }
        }
    }

}
