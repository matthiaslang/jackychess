package org.mattlang.jc.engine.evaluation.annotation;

import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.engine.evaluation.annotation.configure.*;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.extern.java.Log;


@Log
public class EvalConfigurator implements EvalConfigVisitor {

    private EvalConfig config;
    private ParameterizedEvaluation eval;

    public EvalConfigurator(EvalConfig config) {
        this.config = config;
    }

    public void configure(ParameterizedEvaluation eval) {
        this.eval = eval;

        ParamConfiguratorTraverser traverser = new ParamConfiguratorTraverser(this);
        traverser.traverse(eval, new ConfigFilter(".*"));
    }

    @Override
    public void visitMgEgIntProperty(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        int intMg = config.getIntProp(configDefinition.getFullQualifiedPropNameMg());
        int intEg = config.getIntProp(configDefinition.getFullQualifiedPropNameEg());
        int intVal = MgEgScore.createMgEgScore(intMg, intEg);
        fieldAccessStack.set(eval, intVal);
    }

    @Override
    public void visitIntProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        int intVal = config.getIntProp(configDefinition.getFullQualifiedConfigName());
        fieldAccessStack.set(eval, intVal);
    }

    @Override
    public void visitFloatProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack) {
        float floatVal = config.getFloatProp(configDefinition.getFullQualifiedConfigName());
        fieldAccessStack.set(eval, floatVal);
    }

    @Override
    public void visitFloatArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack) {
        FloatArrayFunction floatArr = config.parseFloatArray(configDefinition.getFullQualifiedConfigName());
        fieldAccessStack.set(eval, floatArr);
    }

    @Override
    public void visitMgEgArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall) {
        MgEgArrayFunction mgegF = new MgEgArrayFunction(config, configDefinition.getFullQualifiedPropNameMg(),
                configDefinition.getFullQualifiedPropNameEg());
        fieldAccessStack.set(eval, mgegF);
    }

    @Override
    public void visitMgEgPattern(PatternConfigDefinition patternConfigDefinition,
            FieldAccessStack fieldAccessStack, EvalValueInterval intIntervall) {
        Pattern patternMg =
                loadFromFullPath(
                        config.getConfigDir() + patternConfigDefinition.getCsvTablePathNameMg());
        Pattern patternEg =
                loadFromFullPath(
                        config.getConfigDir() + patternConfigDefinition.getCsvTablePathNameEg());
        Pattern patVal = Pattern.combine(patternMg, patternEg);
        fieldAccessStack.set(eval, patVal);
    }

    @Override
    public void visitPattern(PatternConfigDefinition patternConfigDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall) {
        Pattern patVal =
                loadFromFullPath(
                        config.getConfigDir() + patternConfigDefinition.getCsvTablePathName());
        fieldAccessStack.set(eval, patVal);
    }

    @Override
    public void visitArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        ArrayFunction fMg = config.parseArray(configDefinition.getFullQualifiedPropNameMg());
        ArrayFunction fEg = config.parseArray(configDefinition.getFullQualifiedPropNameEg());
        ArrayFunction fVal = ArrayFunction.combine(fMg, fEg);
        fieldAccessStack.set(eval, fVal);
    }

    @Override
    public void visitArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval) {
        ArrayFunction fVal = config.parseArray(configDefinition.getFullQualifiedConfigName());
        fieldAccessStack.set(eval, fVal);
    }
}
