package org.mattlang.jc.perftests;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chessTests.EigenmannRapidEngineChessIT;
import org.mattlang.jc.chessTests.EpdParsing;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

/**
 * benchmarks to analyze sort order changes during development.
 */
public class SearchOptsBenchmarkAnalzyer {

    public static final int MAX_DEPTH = 12;
    public static final int TIMEOUT = 600000;

    /**
     * Not really a test therefore ignored; just to analyze the pruning of nodes depending on move order.
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void compareSpeed() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();

        List<TestPosition> positions = EpdParsing.convertTests(EigenmannRapidEngineChessIT.eret);
        //       positions.addAll(EpdParsing.convertTests(BratKoKopecIT.bratkoKopec));
        EngineBenchmarksRunner runner = new EngineBenchmarksRunner(positions);

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

        int nodesVisited = 0;
        for (BenchmarkResults result : runner.getResults()) {
            nodesVisited += ((BenchmarkIterativeResults) result).getNodesVisited();
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }
        System.out.println("nodes visited " + nodesVisited);

        //        runner.writeCsvReport("target/searchOptsBenchmark2.csv");

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
                .boards.set(() -> new BitBoard());

        return searchParameter;
    }

}
