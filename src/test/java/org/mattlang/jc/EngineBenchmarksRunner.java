package org.mattlang.jc;

import static org.mattlang.jc.Benchmarks.benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.IterativeSearchResult;
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

    public void benchmarkExecute(SearchParameter searchParameter) {
        gameContext = new GameContext();
        Factory.setDefaults(searchParameter);

        for (String position : POSITIONS) {
            results.add(benchmarkRun(position));
        }
    }

    public void benchmarkSingleExecute(SearchParameter searchParameter) {
        gameContext = new GameContext();
        Factory.setDefaults(searchParameter);

        for (String position : POSITIONS) {
            results.add(benchmarkRun(position, 1));
        }
    }

    private BenchmarkIterativeResults benchmarkRun(String fenposition) {
        return benchmarkRun(fenposition, 10);
    }

    private BenchmarkIterativeResults benchmarkRun(String fenposition, int count) {

        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition(fenposition);
        System.out.println(engine.getBoard().toUniCodeStr());
        String name = generateNameFromOptions();

        ExecResults<IterativeSearchResult> execResults = benchmark(
                name,
                () -> engine.goIterative(state, gameContext), count);
        Map stats = Factory.getDefaults().collectStatistics();

        return new BenchmarkIterativeResults(name, execResults, stats, fenposition);
    }

    private String generateNameFromOptions() {
        StringBuilder b = new StringBuilder();
        for (UCIOption option : Factory.getDefaults().getConfig().getAllOptions().getAllOptions()) {
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
