package org.mattlang.jc.perftests;

import static org.mattlang.jc.Benchmarks.benchmark;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.*;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.moves.MoveListPool;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.uci.UCICheckOption;
import org.mattlang.jc.uci.UCIOption;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import lombok.Getter;

/**
 * benchmarks different move ordering options to measure their performance.
 */
public class MoveListImplNegaMaxBenchmark {

    public static final int MAX_DEPTH = 5;
    public static final int TIMEOUT = 60000;

    public static final String[] POSITIONS = {
            "position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w - - 0 0",
            "position fen r4rk1/p2n1ppp/bqp1p3/8/5n1P/b1P2QP1/PP1R1P2/1R4K1 w - - 0 0",
            "position fen 1nk1r1r1/pp2n1pp/4p3/q2pPp1N/b1pP1P2/B1P2R2/2P1B1PP/R2Q2K1 w - - 0 0",
            "position fen 4b3/p3kp2/6p1/3pP2p/2pP1P2/4K1P1/P3N2P/8 w - - 0 0",
            "position fen 2kr1bnr/pbpq4/2n1pp2/3p3p/3P1P1B/2N2N1Q/PPP3PP/2KR1B1R w - - 0 0"
    };

    GameContext gameContext = new GameContext();

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
        ArrayList<Results> results = new ArrayList<>();

        // normal
        benchmarkExecute(normalParams(), results);

        // now with new movelistimpl and moveimpl
        benchmarkExecute(
                normalParams()
                        .moveList.set(() -> MoveListPool.instance.newOne()),
                results);

      
        for (Results result : results) {
            System.out.println(result.name + ": " + result.watch.getFormattedDuration());
        }

        writeCsvReport(results);

    }

    private SearchParameter normalParams() {
        SearchParameter searchParameter = Factory.createIterativeDeepeningPVS()
                .config(c -> c.timeout.setValue(TIMEOUT))
                .config(c -> c.activatePvsSearch.setValue(true))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.maxDepth.setValue(MAX_DEPTH))
                .config(c -> c.useTTCache.setValue(true))
                .config(c -> c.useKillerMoves.setValue(true))
                .config(c -> c.useHistoryHeuristic.setValue(true))
                .config(c -> c.useMvvLvaSorting.setValue(true))
                .config(c -> c.usePvSorting.setValue(true))
                .boards.set(() -> new Board3());

        return searchParameter;
    }

    private void benchmarkExecute(SearchParameter searchParameter, List<Results> results) {
        Factory.setDefaults(searchParameter);

        for (String position : POSITIONS) {
            results.add(benchmarkRun(position));
        }
    }

    private Results benchmarkRun(String fenposition) {

        Engine engine = new Engine();
        GameState state = engine.getBoard().setFenPosition(fenposition);
        System.out.println(engine.getBoard().toUniCodeStr());
        String name = generateNameFromOptions();

        StopWatch watch = benchmark(
                name,
                () -> {
                    // now starting engine:
                    Move move = engine.go(state, gameContext);
                });
        Map stats = Factory.getDefaults().collectStatistics();

        return new Results(name, watch, stats, fenposition);
    }

    private String generateNameFromOptions() {
        StringBuilder b = new StringBuilder();
        for (UCIOption option : Factory.getDefaults().getConfig().getAllOptions().values()) {
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

    @Getter
    public static class Results {

        String name;
        StopWatch watch;
        private final long duration;
        private final String formattedDuration;
        private final String fenposition;
        Map stats;
        private int depth;

        private final String evaluateFunction;
        private final Boolean useMvvLvaSorting;
        private final Boolean usePvSorting;
        private final Boolean useTTCache;
        private final Boolean useKillerMoves;
        private final Boolean useHistoryHeuristic;
        private final Boolean pvSearch;
        private final Integer maxQuiescence;
        private final String searchAlgorithm;
        private final String moveListImpl;

        public Results(String name, StopWatch watch, Map stats, String fenposition) {
            this.name = name;
            this.watch = watch;
            this.duration = watch.getDuration();
            this.formattedDuration = watch.getFormattedDuration();
            this.stats = stats;
            this.fenposition = fenposition;

            ConfigValues config = Factory.getDefaults().getConfig();
            this.depth = config.maxDepth.getValue();
            this.evaluateFunction = config.evluateFunctions.getValue().name();
            this.useMvvLvaSorting = config.useMvvLvaSorting.getValue();
            this.usePvSorting = config.usePvSorting.getValue();
            this.useTTCache = config.useTTCache.getValue();
            this.useKillerMoves = config.useKillerMoves.getValue();
            this.useHistoryHeuristic = config.useHistoryHeuristic.getValue();
            this.pvSearch = config.activatePvsSearch.getValue();
            this.maxQuiescence = config.maxQuiescence.getValue();
            this.searchAlgorithm = config.searchAlgorithm.getValue().name();
            this.moveListImpl = Factory.getDefaults().moveList.instance().getClass().getName();

        }
    }

    public static final String[] props =
            { "name", "fenposition", "depth", "maxQuiescence", "duration", "formattedDuration", "searchAlgorithm",
                    "evaluateFunction",
                    "useMvvLvaSorting", "usePvSorting", "useTTCache", "useKillerMoves", "useHistoryHeuristic",
                    "pvSearch" , "moveListImpl"};

    private void writeCsvReport(ArrayList<Results> results) throws IOException {
        try (Writer writer = new FileWriter(new File("target/moveimplBenchmark.csv"));
                CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            csvWriter.writeHeader(props);
            for (Results result : results) {
                csvWriter.write(result, props);
            }
        }
    }
}
