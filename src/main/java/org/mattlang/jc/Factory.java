package org.mattlang.jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.MultiThreadedIterativeDeepening;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

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

    public static SearchParameter createStable() {
        return new SearchParameter()
                .evaluateFunction.set(MinimalPstEvaluation::new)
                .legalMoveGenerator.set(PseudoLegalMoveGenerator::new)
                .boards.set(BitBoard::new)
                .checkChecker.set(BBCheckCheckerImpl::new)
                .searchMethod.set(()->new IterativeDeepeningPVS(new NegaMaxAlphaBetaPVS().setDoPVSSearch(true)))
                .config(c -> {
                    c.maxDepth.setValue(40);
                    c.maxQuiescence.setValue(10);
                    c.timeout.setValue(15000);
                    c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED);
                    c.evaluateParamSet.setValue(EvalParameterSet.CURRENT);
                });
    }


    public static SearchParameter createMultiThread() {
        return new SearchParameter()
                .evaluateFunction.set(MinimalPstEvaluation::new)
                .legalMoveGenerator.set(PseudoLegalMoveGenerator::new)
                .boards.set(BitBoard::new)
                .checkChecker.set(BBCheckCheckerImpl::new)
                .searchMethod.set(()->new MultiThreadedIterativeDeepening())
                .config(c -> {
                    c.maxDepth.setValue(40);
                    c.maxQuiescence.setValue(10);
                    c.timeout.setValue(15000);
                    c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED);
                    c.evaluateParamSet.setValue(EvalParameterSet.CURRENT);
                });
    }

    public static SearchParameter createDefaultParameter() {
        return createStable();
    }

    private static SearchParameter defaults = createDefaultParameter();

    public static SearchParameter getDefaults() {
        return defaults;
    }

    public static void setDefaults(SearchParameter defaultParameter) {
        Factory.defaults = defaultParameter;
    }
}
