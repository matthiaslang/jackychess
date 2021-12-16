package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.uci.UCI;

/**
 * benchmarks to analyze pvs search and null move.
 */
public class SearchOptsBenchmark2 {

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

        initLogging();
        UCI.instance.attachStreams();

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner();

        // all opts deactivated
        runner.benchmarkSingleExecute(everythingOff());

        // everything off, but cache:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.useTTCache.setValue(true)));

        // pv sorting, pv search with cache
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache, except pv search and sort:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache +  nullMove:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/searchOptsBenchmark2.csv");

    }

    private SearchParameter everythingOff() {
        SearchParameter searchParameter = Factory.createBitboard()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                .config(c -> c.usePvSorting.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .boards.set(() -> new BitBoard());

        return searchParameter;
    }

}
