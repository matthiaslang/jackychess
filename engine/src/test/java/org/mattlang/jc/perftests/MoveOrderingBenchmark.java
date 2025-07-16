package org.mattlang.jc.perftests;

import static org.mattlang.jc.ConfigValues.getConfigValues;
import static org.mattlang.jc.util.Logging.initLogging;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.BenchmarkResults;
import org.mattlang.jc.EngineBenchmarksRunner;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.uci.UCI;

/**
 * benchmarks different move ordering options to measure their performance.
 */
public class MoveOrderingBenchmark {

    public static final int MAX_DEPTH = 4;
    public static final int TIMEOUT = 60000;

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     *
     * @throws IOException
     */
    @Test
    public void compareSpeed() throws IOException {

        initLogging();
        UCI.instance.attachStreams();

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner();

        everythingOff();
        // all opts deactivated
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // pv sorting, pv search
        everythingOff();
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // pv sorting, pv search, mvvlva
        everythingOff();
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // only mvvlva
        everythingOff();
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // only killer moves
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // only history heuristic
        everythingOff();
        getConfigValues().useHistoryHeuristic.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // everything on:
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // everything on + cache:
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // everything on + cache + aspiration:
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        // "old" version: pv search, pv order, mmvla + cache:
        everythingOff();
        runner.benchmarkExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/moveOrderingBenchmark.csv");

    }

    private void everythingOff() {

        getConfigValues().useKillerMoves.setValue(false);
        getConfigValues().useHistoryHeuristic.setValue(false);

    }

}
