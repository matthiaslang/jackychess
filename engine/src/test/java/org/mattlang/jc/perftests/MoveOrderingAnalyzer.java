package org.mattlang.jc.perftests;

import static org.mattlang.jc.ConfigValues.getConfigValues;
import static org.mattlang.jc.util.Logging.initLogging;
import static org.mattlang.tuning.data.epdparser.EpdParser.parseEPDTests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.BenchmarkIterativeResults;
import org.mattlang.jc.EngineBenchmarksRunner;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.uci.UCI;

/**
 * benchmarks different move ordering options to measure their performance.
 */
@Ignore
public class MoveOrderingAnalyzer {

    public static final int MAX_DEPTH = 9;
    public static final int TIMEOUT = 60000;

    /**
     * Compares speed between "default" alpha beta deepening and the variant with TT cache and zobrist hashing.
     * the opt variatn is slightly faster on depth > 7
     *
     * @throws IOException
     */
    @Test
    public void compareOrderEffects() throws IOException, URISyntaxException {

        initLogging();
        UCI.instance.attachStreams();

        URL resource = MoveOrderingAnalyzer.class.getResource("/quiet-labeled_debug.epd");
        File file = new File(resource.toURI());
        String fileContent = Files.readString(file.toPath());

        List<String> epds = Arrays.asList(fileContent.split("\n"));
        List<TestPosition> positions = parseEPDTests(fileContent).stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());


        EngineBenchmarksRunner runner = new EngineBenchmarksRunner(positions);

        // normal run
        System.setProperty("TESTFEATURE", "NORMAL");
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useMvvLvaSorting.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        getConfigValues().useTTCache.setValue(true);
        getConfigValues().aspiration.setValue(true);
        runner.benchmarkSingleExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));

        System.setProperty("TESTFEATURE", "ANewFeature");
        // run with a new feature
        everythingOff();
        getConfigValues().useKillerMoves.setValue(true);
        getConfigValues().useMvvLvaSorting.setValue(true);
        getConfigValues().useHistoryHeuristic.setValue(true);
        getConfigValues().useTTCache.setValue(true);
        getConfigValues().aspiration.setValue(true);
        runner.benchmarkSingleExecute(new SearchParameter(TIMEOUT, MAX_DEPTH));


        // check stability of results:
        Map<String, List<BenchmarkIterativeResults>> resultsByFen = runner.getResults().stream()
                .filter(e -> e.getFenposition() != null)
                .collect(Collectors.groupingBy(BenchmarkIterativeResults::getFenposition));

        Map<String, Integer> stability = resultsByFen.values().stream()
                .map(l -> l.get(0).getMove().equals(l.get(1).getMove()) ? "stable" : "unstable")
                .collect(Collectors.toMap(Function.identity(), e -> 1, Math::addExact));

        System.out.println("Stable results: " + stability.get("stable"));
        System.out.println("Instable results: " + stability.get("unstable"));

        runner.getResults().stream().filter(e -> e.getName().equals("NORMAL"))
                .findFirst()
                .ifPresent(e -> {
                    System.out.println("normal, nodes searched:      " + e.getNodesVisited());
                    System.out.println("normal, quiescence nodes searched:      " + e.getQuiescenceNodesVisited());
                });


        runner.getResults().stream().filter(e -> e.getName().equals("ANewFeature"))
                .findFirst()
                .ifPresent(e -> {
                    System.out.println("new Feature, nodes searched: " + e.getNodesVisited());
                    System.out.println("new Feature, quiescence nodes searched: " + e.getQuiescenceNodesVisited());
                });

        runner.writeCsvReport("target/moveOrderingBenchmark.csv");

    }

    private void everythingOff() {

        getConfigValues().useTTCache.setValue(false);
        getConfigValues().aspiration.setValue(false);
        getConfigValues().useKillerMoves.setValue(false);
        getConfigValues().useHistoryHeuristic.setValue(false);
        getConfigValues().useMvvLvaSorting.setValue(false);

    }

}
