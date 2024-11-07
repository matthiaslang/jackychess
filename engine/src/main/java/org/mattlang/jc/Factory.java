package org.mattlang.jc;

import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.MultiThreadedIterativeDeepening;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;

/**
 * Factory to switch between different implementations (mainly for tests).
 */
public class Factory {

    public static SearchParameter createStable() {
        return new SearchParameter()
                .searchMethod.set(() -> new IterativeDeepeningPVS(new NegaMaxAlphaBetaPVS()))
                .config(c -> {
                    c.timeout.setValue(15000);
                });
    }

    public static SearchParameter createMultiThread() {
        return new SearchParameter()
                .searchMethod.set(() -> new MultiThreadedIterativeDeepening())
                .config(c -> {
                    c.timeout.setValue(15000);
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
