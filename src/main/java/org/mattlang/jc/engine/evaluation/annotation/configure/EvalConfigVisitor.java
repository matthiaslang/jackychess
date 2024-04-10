package org.mattlang.jc.engine.evaluation.annotation.configure;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;

public interface EvalConfigVisitor {

    void visitMgEgIntProperty(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval definedInterval);

    void visitIntProperty(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval);

    void visitFloatProperty(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam);

    void visitFloatArray(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam);

    void visitMgEgArray(String propMg, String propEg, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval intIntervall);

    void visitMgEgPattern(String subDir, String tableCsvPathNameMg, String tableCsvPathNameEg,
            FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam, EvalValueInterval intIntervall);

    void visitPattern(String subDir, String tableCsvPathName, FieldAccessStack fieldAccessStack,
            EvalConfigParam evalConfigParam, EvalValueInterval intIntervall);

    void visitArray(String propMg, String propEg, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval);

    void visitArray(String qualifiedName, FieldAccessStack fieldAccessStack, EvalConfigParam evalConfigParam,
            EvalValueInterval definedInterval);
}
