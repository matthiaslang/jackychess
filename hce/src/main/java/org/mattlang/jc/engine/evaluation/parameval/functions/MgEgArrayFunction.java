package org.mattlang.jc.engine.evaluation.parameval.functions;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;

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

    private final int[] combinedMgEg;

    public MgEgArrayFunction(EvalConfig config, String propertyMg, String propertyEg) {
        this.propertyMg = propertyMg;
        this.propertyEg = propertyEg;
        functionMg = config.parseArray(propertyMg);
        functionEg = config.parseArray(propertyEg);
        combinedMgEg = ArrayFunction.combineArr(functionMg, functionEg);

    }

    public int calc(int x) {
        return combinedMgEg[x];
    }

    public void updateCombinedValue(int pos) {
        combinedMgEg[pos] = MgEgScore.createMgEgScore(functionMg.calc(pos), functionEg.calc(pos));
    }

}
