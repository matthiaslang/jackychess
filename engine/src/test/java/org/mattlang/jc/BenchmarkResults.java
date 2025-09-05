package org.mattlang.jc;

import lombok.Getter;
import org.mattlang.jc.engine.Configurator;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class BenchmarkResults<T> {

    private final Boolean aspiration;
    private final Boolean useNullMoves;
    private String testName;
    private String testExpectedBestMove;
    private final Boolean staticNullMove;
    private final Boolean razoring;
    private final Boolean useLateMoveReductions;
    private final Boolean deltaCutoff;

    String name;
    StopWatch watch;
    private final ExecResults<T> execResult;

    private final long duration;
    private final String formattedDuration;
    private String fenposition;
    Map stats;

    private final String evaluateFunction;
    private final Boolean useMvvLvaSorting;
    private final Boolean useKillerMoves;
    private final Boolean useHistoryHeuristic;
    private final Boolean pvSearch;
    private final Integer maxQuiescence;


    public BenchmarkResults(String name, ExecResults<T> execResults, Map stats, TestPosition testPosition) {
        this.name = name;
        this.watch = execResults.getWatch();
        this.execResult = execResults;
        this.duration = watch.getDuration();
        this.formattedDuration = watch.getFormattedDuration();
        this.stats = stats;
        if (testPosition != null) {
            this.fenposition = testPosition.getFen();
            this.testName = testPosition.getName();
            this.testExpectedBestMove = testPosition.getExpectedBestMoves().stream().map(b->b.toStr()).collect(Collectors.joining(","));
        }
        ConfigValues config = ConfigValues.getConfigValues();
        this.evaluateFunction = Configurator.determineEvalImplName();
        this.useMvvLvaSorting = true;
        this.useKillerMoves = true;
        this.useHistoryHeuristic = true;
        this.pvSearch = true;
        this.maxQuiescence =0;
        this.aspiration = true;
        this.useNullMoves = true;
        this.staticNullMove = true;
        this.razoring = true;
        this.useLateMoveReductions = true;
        this.deltaCutoff = true;

    }

}
