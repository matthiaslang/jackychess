package org.mattlang.jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mattlang.jc.board.Board3;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.IterativeDeepeningNegaMaxAlphaBeta;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

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
                .evaluateFunction.set(DefaultEvaluateFunction::new)
                .moveGenerator.set(MoveGeneratorImpl2::new)
                .legalMoveGenerator.set(LegalMoveGeneratorImpl3::new)
                .boards.set(Board3::new)
                .searchMethod.set(IterativeDeepeningNegaMaxAlphaBeta::new)
                .config(c -> {
                    c.maxDepth.setValue(15);
                    c.maxQuiescence.setValue(2);
                    c.timeout.setValue(15000);
                });
    }

    public static SearchParameter createIterativeDeepeningPVS() {
        return new SearchParameter()
                .evaluateFunction.set(DefaultEvaluateFunction::new)
                .moveGenerator.set(MoveGeneratorImpl2::new)
                .legalMoveGenerator.set(LegalMoveGeneratorImpl3::new)
                .boards.set(Board3::new)
                .searchMethod.set(IterativeDeepeningPVS::new)
                .config(c -> {
                    c.maxDepth.setValue(15);
                    c.maxQuiescence.setValue(2);
                    c.timeout.setValue(15000);
                });
    }

    /**
     * Stable is currently PVS Search (without caching) and with new material eval.
     * @return
     */
    public static SearchParameter createStable() {
        return new SearchParameter()
                .evaluateFunction.set(DefaultEvaluateFunction::new)
                .moveGenerator.set(MoveGeneratorImpl2::new)
                .legalMoveGenerator.set(LegalMoveGeneratorImpl3::new)
                .boards.set(Board3::new)
                .searchMethod.set(()->new IterativeDeepeningPVS(new NegaMaxAlphaBetaPVS().setDoPVSSearch(true)))
                .config(c -> {
                    c.maxDepth.setValue(15);
                    c.maxQuiescence.setValue(2);
                    c.timeout.setValue(15000);
                });
    }


    public static SearchParameter createIterativeDeepeningMtdf() {
        return new SearchParameter()
                .evaluateFunction.set(() -> new DefaultEvaluateFunction())
                .moveGenerator.set(() -> new MoveGeneratorImpl2())
                .legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl3())
                .searchMethod.set(() -> new IterativeDeepeningMtdf())
                .config(c -> {
                    c.maxDepth.setValue(6);
                    c.maxQuiescence.setValue(0);
                    c.timeout.setValue(15000);
                })
                ;
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
