package org.mattlang.jc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import lombok.Getter;

@Getter
public class BenchmarkResults {

    private final Boolean aspiration;
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

    public BenchmarkResults(String name, StopWatch watch, Map stats, String fenposition) {
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
        this.aspiration=config.aspiration.getValue();
        this.moveListImpl = Factory.getDefaults().moveList.instance().getClass().getSimpleName();

    }

    public static final String[] props =
            { "name", "fenposition", "depth", "maxQuiescence", "duration", "formattedDuration", "searchAlgorithm",
                    "evaluateFunction",
                    "useMvvLvaSorting", "usePvSorting", "useTTCache", "useKillerMoves", "useHistoryHeuristic",
                    "pvSearch", "aspiration", "moveListImpl" };

    public static void writeCsvReport(ArrayList<BenchmarkResults> results, String filename) throws IOException {
        try (Writer writer = new FileWriter(new File(filename));
                CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            csvWriter.writeHeader(props);
            for (BenchmarkResults result : results) {
                csvWriter.write(result, props);
            }
        }
    }
}
