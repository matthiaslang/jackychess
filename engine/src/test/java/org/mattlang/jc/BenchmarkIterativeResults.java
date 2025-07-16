package org.mattlang.jc;

import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.AppConfiguration.APPCONFIG;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BenchmarkIterativeResults extends BenchmarkResults<IterativeSearchResult> {

    private String move;

    private int nodesVisited;
    private int quiescenceNodesVisited;

    private String ebfReport;

    public BenchmarkIterativeResults(String name,
            ExecResults<IterativeSearchResult> execResults, Map stats, TestPosition testPosition) {
        super(name, execResults, stats, testPosition);

        move = execResults.getResults().stream().map(r -> r.getSavedMove().toStr()).collect(joining());
        nodesVisited = execResults.getResults().stream().map(r -> r.getRslt().nodesVisited).reduce(0, Integer::sum);
        quiescenceNodesVisited = execResults.getResults()
                .stream()
                .map(r -> r.getRslt().quiescenceNodesVisited)
                .reduce(0, Integer::sum);

        ebfReport = execResults.getResults().stream().map(r -> r.getEbfReport()).collect(joining(";"));
    }


    public static final String[] props =
            { "name", "fenposition", "testName", "testExpectedBestMove", "maxQuiescence", "duration",
                    "formattedDuration",
                    "evaluateFunction",
                    "useMvvLvaSorting",  "useKillerMoves", "useHistoryHeuristic",
                    "pvSearch", "aspiration", "useNullMoves", "staticNullMove", "useLateMoveReductions",
                    "deltaCutoff", "razoring",
                     "move", "nodesVisited",
                    "quiescenceNodesVisited", "ebfReport" };

    public BenchmarkIterativeResults(BenchmarkIterativeResults b1, BenchmarkIterativeResults b2) {
        super(APPCONFIG.getStringValue("TESTFEATURE").orElseThrow(), b1.getExecResult(), b1.stats, null);
        nodesVisited=b1.getNodesVisited()+b2.getNodesVisited();
        quiescenceNodesVisited=b1.getQuiescenceNodesVisited()+b2.getQuiescenceNodesVisited();


    }

    public static void writeCsvReport(ArrayList<BenchmarkIterativeResults> results, String filename)
            throws IOException {
        try (Writer writer = new FileWriter(filename);
                CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            csvWriter.writeHeader(props);
            for (BenchmarkResults result : results) {
                csvWriter.write(result, props);
            }
        }
    }
}
