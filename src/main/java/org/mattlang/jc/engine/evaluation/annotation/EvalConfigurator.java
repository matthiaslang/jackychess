package org.mattlang.jc.engine.evaluation.annotation;

import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.engine.evaluation.annotation.configure.ConfigFilter;
import org.mattlang.jc.engine.evaluation.annotation.configure.EvalConfigVisitor;
import org.mattlang.jc.engine.evaluation.annotation.configure.FieldAccessStack;
import org.mattlang.jc.engine.evaluation.annotation.configure.ParamConfiguratorTraverser;
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
    public void visitMgEgIntProperty(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {
        int intMg = config.getIntProp(propMg);
        int intEg = config.getIntProp(propEg);
        int intVal = MgEgScore.createMgEgScore(intMg, intEg);
        fieldAccessStack.set(eval, intVal);
    }

    @Override
    public void visitIntProperty(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {
        int intVal = config.getIntProp(qualifiedName);
        fieldAccessStack.set(eval, intVal);
    }

    @Override
    public void visitFloatProperty(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam) {
        float floatVal = config.getFloatProp(qualifiedName);
        fieldAccessStack.set(eval, floatVal);
    }

    @Override
    public void visitFloatArray(String qualifiedName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam) {
        FloatArrayFunction floatArr = config.parseFloatArray(qualifiedName);
        fieldAccessStack.set(eval, floatArr);
    }

    @Override
    public void visitMgEgArray(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval intIntervall) {
        MgEgArrayFunction mgegF = new MgEgArrayFunction(config, propMg, propEg);
        fieldAccessStack.set(eval, mgegF);
    }

    @Override
    public void visitMgEgPattern(String subDir, String tableCsvPathNameMg, String tableCsvPathNameEg,
            FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam, EvalValueInterval intIntervall) {
        Pattern patternMg =
                loadFromFullPath(
                        config.getConfigDir() + tableCsvPathNameMg);
        Pattern patternEg =
                loadFromFullPath(
                        config.getConfigDir() + tableCsvPathNameEg);
        Pattern patVal = Pattern.combine(patternMg, patternEg);
        fieldAccessStack.set(eval, patVal);
    }

    @Override
    public void visitPattern(String subDir, String tableCsvPathName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval intIntervall) {
        Pattern patVal =
                loadFromFullPath(
                        config.getConfigDir() + tableCsvPathName);
        fieldAccessStack.set(eval, patVal);
    }

    @Override
    public void visitArray(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval) {
        ArrayFunction fMg = config.parseArray(propMg);
        ArrayFunction fEg = config.parseArray(propEg);
        ArrayFunction fVal = ArrayFunction.combine(fMg, fEg);
        fieldAccessStack.set(eval, fVal);
    }

    @Override
    public void visitArray(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval) {
        ArrayFunction fVal = config.parseArray(qualifiedName);
        fieldAccessStack.set(eval, fVal);
    }
}
