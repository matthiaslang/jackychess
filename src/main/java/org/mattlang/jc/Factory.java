package org.mattlang.jc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.evaluation.minimalpst.MinimalPstEvaluation;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;
import org.mattlang.jc.movegenerator.*;

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

    public static SearchParameter createIterativeDeepeningPVS() {
        return new SearchParameter()
                .evaluateFunction.set(DefaultEvaluateFunction::new)
                .moveGenerator.set(MoveGeneratorImpl3::new)
                .legalMoveGenerator.set(LegalMoveGeneratorImpl3::new)
                .boards.set(BitBoard::new)
                .searchMethod.set(IterativeDeepeningPVS::new)
                .config(c -> {
                    c.maxDepth.setValue(15);
                    c.maxQuiescence.setValue(2);
                    c.timeout.setValue(15000);
                });
    }

    /**
     * Same as stable only with Bitboard implementation and bitboard move generator
     * @return
     */
    public static SearchParameter createBitboard() {
        return new SearchParameter()
                .evaluateFunction.set(DefaultEvaluateFunction::new)
                .moveGenerator.set(BBMoveGeneratorImpl::new)
                .legalMoveGenerator.set(BBLegalMoveGeneratorImpl::new)
                .boards.set(BitBoard::new)
                .checkChecker.set(BBCheckCheckerImpl::new)
                .searchMethod.set(()->new IterativeDeepeningPVS(new NegaMaxAlphaBetaPVS().setDoPVSSearch(true)))
                .config(c -> {
                    c.maxDepth.setValue(15);
                    c.maxQuiescence.setValue(2);
                    c.timeout.setValue(15000);
                });
    }

    public static SearchParameter createStable() {
        return new SearchParameter()
                .evaluateFunction.set(MinimalPstEvaluation::new)
                .moveGenerator.set(MoveGeneratorImpl3::new)
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
                .moveGenerator.set(() -> new MoveGeneratorImpl3())
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
        return createIterativeDeepeningPVS();
    }

    private static SearchParameter defaults = createDefaultParameter();

    public static SearchParameter getDefaults() {
        return defaults;
    }

    public static void setDefaults(SearchParameter defaultParameter) {
        Factory.defaults = defaultParameter;
    }
}
