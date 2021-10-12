package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.Board3;
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

        // all opts deactivated
        runner.benchmarkExecute(everythingOff());

        // only pv sorting
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.usePvSorting.setValue(true)));

        // pv sorting, pv search
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.activatePvsSearch.setValue(true)));

        // pv sorting, pv search, mvvlva
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true)));

        // only mvvlva
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.useMvvLvaSorting.setValue(true)));

        // only killer moves
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.useKillerMoves.setValue(true)));

        // only history heuristic
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.useHistoryHeuristic.setValue(true)));

        // everything on:
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true)));

        // everything on + cache:
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache + aspiration:
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // "old" version: pv search, pv order, mmvla + cache:
        runner.benchmarkExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.usePvSorting.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/moveOrderingBenchmark.csv");

    }

    private SearchParameter everythingOff() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                .config(c -> c.usePvSorting.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .boards.set(() -> new Board3());

        return searchParameter;
    }

}
