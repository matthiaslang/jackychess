package org.mattlang.jc.perftests;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.chesstests.EigenmannRapidEngineChess;
import org.mattlang.jc.chesstests.EpdParser;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

/**
 * benchmarks to analyze pvs search and null move.
 */
public class SearchOptsBenchmark2 {

    public static final int MAX_DEPTH = 6;
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

        List<TestPosition> positions = EpdParser.convertTests(EigenmannRapidEngineChess.EIGENMANN_RAPID);
        EngineBenchmarksRunner runner = new EngineBenchmarksRunner(positions);

        // all opts deactivated
        //        runner.benchmarkSingleExecute(everythingOff());

        // everything off, but cache:
        //        runner.benchmarkSingleExecute(
        //                everythingOff()
        //                        .config(c -> c.useTTCache.setValue(true)));

        // pv sorting, pv search with cache
        //        runner.benchmarkSingleExecute(
        //                everythingOff()
        //                        .config(c -> c.usePvSorting.setValue(true))
        //                        .config(c -> c.activatePvsSearch.setValue(true))
        //                        .config(c -> c.useTTCache.setValue(true)));

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
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache + check extension:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.chessExtension.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        // everything on + cache +  nullMove:
        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.staticNullMove.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.staticNullMove.setValue(true))
                        .config(c -> c.useLateMoveReductions.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.staticNullMove.setValue(true))
                        .config(c -> c.useLateMoveReductions.setValue(true))
                        .config(c -> c.deltaCutoff.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute(
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.staticNullMove.setValue(true))
                        .config(c -> c.useLateMoveReductions.setValue(true))
                        .config(c -> c.deltaCutoff.setValue(true))
                        .config(c -> c.razoring.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/searchOptsBenchmark2.csv");

    }

    private SearchParameter everythingOff() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                ;

        return searchParameter;
    }

}
