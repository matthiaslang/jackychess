package org.mattlang.jc.perftests;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.attic.movegenerator.BBLegalMoveGeneratorImpl;
import org.mattlang.jc.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.uci.UCI;

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

        initLogging();
        UCI.instance.attachStreams();

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner();

        // normal
        runner.benchmarkExecute(normalParams());

        // now with new bitboard
        runner.benchmarkExecute(
                normalParams()
                        .boards.set(() -> new BitBoard())
                        .legalMoveGenerator.set(() -> new BBLegalMoveGeneratorImpl())
                        .config(c -> c.searchAlgorithm.setValue(SearchAlgorithms.SINGLETHREAD)));


        // normal with all opts
        runner.benchmarkExecute(allOptsOn());

        // normal with all opts and bitboards
        runner.benchmarkExecute(allOptsOn()  .boards.set(() -> new BitBoard())
                .legalMoveGenerator.set(() -> new BBLegalMoveGeneratorImpl())
                .config(c -> c.searchAlgorithm.setValue(SearchAlgorithms.SINGLETHREAD)));

        for (BenchmarkResults result : runner.getResults()) {
            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
        }

        runner.writeCsvReport("target/bbmoveBenchmark.csv");

    }

    private SearchParameter normalParams() {
        SearchParameter searchParameter = Factory.createIterativeDeepeningPVS()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.useKillerMoves.setValue(false))
                .config(c -> c.useHistoryHeuristic.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .config(c -> c.useMvvLvaSorting.setValue(false))
                .config(c -> c.usePvSorting.setValue(false))
                .boards.set(() -> new BitBoard());

        return searchParameter;
    }
    private SearchParameter allOptsOn() {
        SearchParameter searchParameter = Factory.createIterativeDeepeningPVS()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(true))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.useKillerMoves.setValue(true))
                .config(c -> c.aspiration.setValue(true))
                .config(c -> c.useHistoryHeuristic.setValue(true))
                .config(c -> c.useMvvLvaSorting.setValue(true))
                .config(c -> c.usePvSorting.setValue(true))
                .boards.set(() -> new BitBoard());

        return searchParameter;
    }
}
