package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.uci.UCI;

/**
 * benchmarks different search options and measure their cut off rate, searched nodes rate, etc.
 */
public class SearchOptsBenchmark {

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

        // all opts deactivated
        runner.benchmarkSingleExecute(everythingOff());

        // pv sorting, pv search
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true)));

        // pv sorting, pv search, mvvlva
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true)));

        // only mvvlva
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.useMvvLvaSorting.setValue(true)));

        // only killer moves
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.useKillerMoves.setValue(true)));

        // only history heuristic
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.useHistoryHeuristic.setValue(true)));

        // everything on:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true)));

        // everything on + cache:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache + aspiration:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache + aspiration + nullMove:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/searchOptsBenchmark.csv");

    }

    private SearchParameter everythingOff() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .boards.set(() -> new BitBoard());

        return searchParameter;
    }

}
