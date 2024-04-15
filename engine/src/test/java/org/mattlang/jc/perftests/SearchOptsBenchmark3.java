package org.mattlang.jc.perftests;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chessTests.EigenmannRapidEngineChessIT;
import org.mattlang.jc.chessTests.EpdParsing;
import org.mattlang.jc.uci.UCI;

/**
 * benchmarks to analyze sorting by history, capture history...
 */
public class SearchOptsBenchmark3 {

    public static final int MAX_DEPTH = 10;
    public static final int TIMEOUT = 600000;

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

        List<TestPosition> positions = EpdParsing.convertTests(EigenmannRapidEngineChessIT.eret);

        EngineBenchmarksRunner runner = new EngineBenchmarksRunner(positions);


        runner.benchmarkSingleExecute("noHistory   ",
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(false))
//                        .config(c -> c.useCaptureHeuristic.setValue(false))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute("captHist    ",
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
                        //                        .config(c -> c.useCaptureHeuristic.setValue(true))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));

        runner.benchmarkSingleExecute("history     ",
                everythingOff()
                        .config(c -> c.activatePvsSearch.setValue(true))
                        .config(c -> c.useKillerMoves.setValue(true))
                        .config(c -> c.useMvvLvaSorting.setValue(true))
                        .config(c -> c.useHistoryHeuristic.setValue(true))
//                        .config(c -> c.useCaptureHeuristic.setValue(false))
                        .config(c -> c.aspiration.setValue(true))
                        .config(c -> c.useNullMoves.setValue(true))
                        .config(c -> c.useTTCache.setValue(true)));


        Map<String, Integer> nodesByTest = runner.getResults()
                .stream()
                .collect(groupingBy(r -> r.getName(),   LinkedHashMap::new,summingInt(r -> r.getNodesVisited())));

        System.out.println("----------------------------------------------------------------");
        DecimalFormat  formatter = new DecimalFormat("###.###");
        nodesByTest.forEach((name,count) ->{
            System.out.println(name + "\t: " + formatter.format(count));
        });
        System.out.println("----------------------------------------------------------------");

//        for (BenchmarkResults result : runner.getResults()) {
//            System.out.println(result.getName() + ": " + result.getWatch().getFormattedDuration());
//        }

        runner.writeCsvReport("target/searchOptsBenchmark.csv");

    }

    private SearchParameter everythingOff() {
        SearchParameter searchParameter = Factory.createStable()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.CURRENT))
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
