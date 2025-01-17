package org.mattlang.jc.perftests;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.BenchmarkResults;
import org.mattlang.jc.EngineBenchmarksRunner;
import org.mattlang.jc.Factory;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

/**
 * benchmarks different move ordering options to measure their performance.
 */
public class AspirationWindowBenchmark {

    public static final int MAX_DEPTH = 5;
    public static final int TIMEOUT = 60000;

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     *
     * @throws IOException
     */
    @Test
    public void compareSpeed() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner();

        // no pvsearch, no aspiration
        runner.benchmarkExecute(
                everythingOn()
                        .config(c -> c.activatePvsSearch.setValue(false))
                        .config(c -> c.aspiration.setValue(false)));

        // only pv search:
        runner.benchmarkExecute(
                everythingOn()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.aspiration.setValue(false)));

        // pv search +  aspiration:
        runner.benchmarkExecute(
                everythingOn()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.aspiration.setValue(true)));

        // aspiration without pv search:
        runner.benchmarkExecute(
                everythingOn()
                        .config(c -> c.activatePvsSearch.setValue(false))
                        .config(c -> c.aspiration.setValue(true)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/aspirationBenchmark.csv");

    }

    private SearchParameter everythingOn() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(true))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.useKillerMoves.setValue(true))
                .config(c -> c.useHistoryHeuristic.setValue(true))
                .config(c -> c.useMvvLvaSorting.setValue(true))
                ;

        return searchParameter;
    }

}
