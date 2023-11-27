package org.mattlang.jc.engine.evaluation.parameval.functions;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;

import lombok.Getter;

/**
 * Wrapper to combine Mg/Eg array functions.
 */
public final class MgEgArrayFunction {

    public final ArrayFunction functionMg;
    public final ArrayFunction functionEg;

    @Getter
    private final String propertyMg;
    @Getter
    private final String propertyEg;

    @Getter
    private final ArrayFunction combinedMgEg;

    public MgEgArrayFunction(EvalConfig config, String propBaseName) {
        this(config, propBaseName, "Mg", "Eg");
    }

    public MgEgArrayFunction(EvalConfig config, String propBaseName, String mgPostfix, String egPostFix) {
        propertyMg = propBaseName + mgPostfix;
        functionMg = config.parseArray(propertyMg);

        propertyEg = propBaseName + egPostFix;
        functionEg = config.parseArray(propertyEg);

        combinedMgEg = ArrayFunction.combine(functionMg, functionEg);

    }

    public int calc(int x) {
        return combinedMgEg.calc(x);
    }

    public void updateCombinedVals() {
        ArrayFunction.updateCombined(combinedMgEg, functionMg, functionEg);
    }
}
