package org.mattlang.jc;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.Benchmarks.benchmark;

import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.parameval.EvalCache;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCICheckOption;
import org.mattlang.jc.uci.UCIOption;

import lombok.Getter;

/**
 * Helper class to run the engine with given search parameters on a bunch of positions and collect the results.
 */
@Getter
public class EngineBenchmarksRunner {

    public static final String[] POSITIONS = {
            "position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w - - 0 0",
            "position fen r4rk1/p2n1ppp/bqp1p3/8/5n1P/b1P2QP1/PP1R1P2/1R4K1 w - - 0 0",
            "position fen 1nk1r1r1/pp2n1pp/4p3/q2pPp1N/b1pP1P2/B1P2R2/2P1B1PP/R2Q2K1 w - - 0 0",
            "position fen 4b3/p3kp2/6p1/3pP2p/2pP1P2/4K1P1/P3N2P/8 w - - 0 0",
            "position fen 2kr1bnr/pbpq4/2n1pp2/3p3p/3P1P1B/2N2N1Q/PPP3PP/2KR1B1R w - - 0 0"
    };

    private GameContext gameContext = new GameContext();

    private ArrayList<BenchmarkIterativeResults> results = new ArrayList<>();

    private List<TestPosition> testPositions = Arrays.stream(POSITIONS).map(p -> new TestPosition(p)).collect(toList());

    public EngineBenchmarksRunner() {
    }

    public EngineBenchmarksRunner(List<TestPosition> testPositions) {
        this.testPositions = testPositions;
    }

    public void benchmarkExecute(SearchParameter searchParameter) {
        gameContext = new GameContext();

        for (TestPosition position : testPositions) {
            results.add(benchmarkRun(position, searchParameter));
        }
    }

    public void benchmarkSingleExecute(SearchParameter searchParameter) {
        gameContext = new GameContext();
        SearchThreadContexts.CONTEXTS.reset();
        ArrayList<BenchmarkIterativeResults> resultsOfThisRun = new ArrayList<>();

        for (TestPosition position : testPositions) {
            BenchmarkIterativeResults result = benchmarkRun(position, searchParameter, 1);
        // for now only add agg results
                        results.add(result);
            resultsOfThisRun.add(result);
        }

        // add a aggregation result:
        Optional<BenchmarkIterativeResults> agg =
                resultsOfThisRun.stream().reduce(new BinaryOperator<BenchmarkIterativeResults>() {

                    @Override
                    public BenchmarkIterativeResults apply(BenchmarkIterativeResults b1,
                            BenchmarkIterativeResults b2) {
                        return new BenchmarkIterativeResults(b1, b2);
                    }
                });

        if (agg.isPresent()) {
            results.add(agg.get());
        }
    }

    public void benchmarkSingleExecute(String name, SearchParameter searchParameter) {
        gameContext = new GameContext();
        SearchThreadContexts.CONTEXTS.reset();
        EvalCache.instance.reset();

        for (TestPosition position : testPositions) {
            results.add(benchmarkRun(name, position, searchParameter, 1));
        }
    }

    private BenchmarkIterativeResults benchmarkRun(TestPosition testPosition, SearchParameter searchParameter) {
        return benchmarkRun(testPosition, searchParameter, 10);
    }

    private BenchmarkIterativeResults benchmarkRun(TestPosition testPosition, SearchParameter searchParameter,
            int count) {

        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition(testPosition.getFenPosition());
//        System.out.println(engine.getBoard().toUniCodeStr());
        String name = generateNameFromOptions();

        // reset caches:
        Caching.CACHING.getTtCache().reset();
        SearchThreadContexts.CONTEXTS.reset();

        ExecResults<IterativeSearchResult> execResults = benchmark(
                name,
                () -> engine.goIterative(searchParameter, state, gameContext), count);
        Map stats = new HashMap();

        return new BenchmarkIterativeResults(name, execResults, stats, testPosition);
    }

    private BenchmarkIterativeResults benchmarkRun(String name, TestPosition testPosition,
            SearchParameter searchParameter, int count) {

        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition(testPosition.getFenPosition());
        //        System.out.println(engine.getBoard().toUniCodeStr());

        ExecResults<IterativeSearchResult> execResults = benchmark(
                name,
                () -> engine.goIterative(searchParameter, state, gameContext), count);
        Map stats = new HashMap();

        return new BenchmarkIterativeResults(name, execResults, stats, testPosition);
    }

    private String generateNameFromOptions() {
        StringBuilder b = new StringBuilder();
        for (UCIOption option : ConfigValues.getConfigValues().getAllOptions().getAllOptions()) {
            if (option instanceof UCICheckOption) {
                if (((UCICheckOption) option).getValue()) {
                    b.append(option.getName());
                    b.append("; ");
                }
            }
        }
        if (b.length() == 0) {
            b.append("no options");
        }
        return b.toString();
    }

    public void writeCsvReport(String filename) throws IOException {
        BenchmarkIterativeResults.writeCsvReport(results, filename);
    }
}
