package org.mattlang.jc.engine.evaluation.annotation.configure;

import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;

public interface EvalConfigVisitor {

    void visitMgEgIntProperty(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval);

    void visitIntProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval);

    void visitFloatProperty(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack);

    void visitFloatArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack);

    void visitMgEgArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall);

    void visitMgEgPattern(PatternConfigDefinition patternConfigDefinition,
            FieldAccessStack fieldAccessStack, EvalValueInterval intIntervall);

    void visitPattern(PatternConfigDefinition patternConfigDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval intIntervall);

    void visitArray(MgEgConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval);

    void visitArray(ConfigDefinition configDefinition, FieldAccessStack fieldAccessStack,
            EvalValueInterval definedInterval);
}
