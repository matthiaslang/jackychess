package org.mattlang.jc.perftests;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

/**
 * benchmarks to measure speed of move generation with bitboard and with old mailbox generation.
 */
public class BitBoardNegaMaxBenchmark {

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

        Logging.initLogging();
        UCI.instance.attachStreams();

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner();

        // normal
        runner.benchmarkExecute(normalParams());

        // now with new bitboard
        runner.benchmarkExecute(
                normalParams()
                        .config(c -> c.searchAlgorithm.setValue(SearchAlgorithms.SINGLETHREAD)));

        // normal with all opts
        runner.benchmarkExecute(allOptsOn());

        // normal with all opts and bitboards
        runner.benchmarkExecute(allOptsOn()
                .config(c -> c.searchAlgorithm.setValue(SearchAlgorithms.SINGLETHREAD)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/bbmoveBenchmark.csv");

    }

    private SearchParameter normalParams() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                ;

        return searchParameter;
    }

    private SearchParameter allOptsOn() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(true))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.useKillerMoves.setValue(true))
                .config(c -> c.aspiration.setValue(true))
                .config(c -> c.useHistoryHeuristic.setValue(true))
                .config(c -> c.useMvvLvaSorting.setValue(true))
                ;

        return searchParameter;
    }
}
