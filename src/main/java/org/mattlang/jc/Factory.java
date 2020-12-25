package org.mattlang.jc;

import org.mattlang.jc.board.Board2;
import org.mattlang.jc.engine.evaluation.CachingEvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.IterativeDeepeningNegaMaxAlphaBeta;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl2;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Factory to switch between different implementations (mainly for tests).
 */
public class Factory {

    private static  Properties appProps = loadAppProps();

    private static Properties loadAppProps() {
        InputStream in = Factory.class.getResourceAsStream("/app.properties");
        appProps = new Properties();
        try {
            appProps.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appProps;
    }

    public static Properties getAppProps() {
        return appProps;
    }

    public static SearchParameter createIterativeDeepeningAlphaBeta() {
        return new SearchParameter()
                .evaluateFunction.set(() -> new CachingEvaluateFunction(new MaterialNegaMaxEval()))
                .moveGenerator.set(() -> new MoveGeneratorImpl2())
                .legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl3())
                .boards.set(() -> new Board2())
                .setMaxDepth(15)
                .setMaxQuiescenceDepth(0)
                .setTimeout(15000)
                .searchMethod.set(() -> new IterativeDeepeningNegaMaxAlphaBeta());
    }

    public static SearchParameter createIterativeDeepeningMtdf() {
        return new SearchParameter()
                .evaluateFunction.set(() -> new CachingEvaluateFunction(new MaterialNegaMaxEval()))
                .moveGenerator.set(() -> new MoveGeneratorImpl())
                .legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl2())
                .setMaxDepth(6)
                .setMaxQuiescenceDepth(0)
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
