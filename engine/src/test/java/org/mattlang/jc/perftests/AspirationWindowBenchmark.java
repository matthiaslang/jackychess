package org.mattlang.jc.perftests;

import static org.mattlang.jc.ConfigValues.getConfigValues;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.BenchmarkResults;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.EngineBenchmarksRunner;
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
        everythingOn();

        getConfigValues().aspiration.setValue(false);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // only pv search:
        everythingOn();
        getConfigValues().aspiration.setValue(false);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // pv search +  aspiration:
        everythingOn();
        getConfigValues().aspiration.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // aspiration without pv search:
        everythingOn();
        getConfigValues().aspiration.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/aspirationBenchmark.csv");

        ConfigValues.resetConfigValues();
    }

    private void everythingOn() {
        getConfigValues().useTTCache.setValue(true);
        getConfigValues().aspiration.setValue(true);
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        getConfigValues().useMvvLvaSorting.setValue(true);
    }

}
