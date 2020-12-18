package org.mattlang.jc;

import org.mattlang.jc.engine.evaluation.CachingEvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.IterativeDeepeningNegaMaxAlphaBeta;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl2;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

/**
 * Factory to switch between different implementations (mainly for tests).
 */
public class Factory {


    public static SearchParameter createIterativeDeepeningAlphaBeta() {
        return new SearchParameter()
                .evaluateFunction.set(() -> new CachingEvaluateFunction(new MaterialNegaMaxEval()))
                .moveGenerator.set(() -> new MoveGeneratorImpl2())
                .legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl2())
                .setMaxDepth(6)
                .setTimeout(15000)
                .searchMethod.set(() -> new IterativeDeepeningNegaMaxAlphaBeta());
    }

    public static SearchParameter createIterativeDeepeningMtdf() {
        return new SearchParameter()
                .evaluateFunction.set(() -> new CachingEvaluateFunction(new MaterialNegaMaxEval()))
                .moveGenerator.set(() -> new MoveGeneratorImpl2())
                .legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl2())
                .setMaxDepth(6)
                .setTimeout(15000)
                .searchMethod.set(() -> new IterativeDeepeningMtdf());
    }

    public static SearchParameter createDefaultParameter() {
        return createIterativeDeepeningAlphaBeta();
    }

    private static SearchParameter defaults = createDefaultParameter();

    public static SearchParameter getDefaults() {
        return defaults;
    }

    public static void setDefaults(SearchParameter defaultParameter) {
        Factory.defaults = defaultParameter;
    }
}
