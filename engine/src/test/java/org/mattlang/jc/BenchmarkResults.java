package org.mattlang.jc;

import java.util.Map;

import org.mattlang.jc.engine.EvalFunctionConfigurator;

import lombok.Getter;

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
    private int depth;

    private final String evaluateFunction;
    private final Boolean useMvvLvaSorting;
    private final Boolean useTTCache;
    private final Boolean useKillerMoves;
    private final Boolean useHistoryHeuristic;
    private final Boolean pvSearch;
    private final Integer maxQuiescence;
    private final String searchAlgorithm;

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
            this.testExpectedBestMove = testPosition.getExpectedBestMove();
        }
        ConfigValues config = Factory.getDefaults().getConfig();
        this.depth = config.maxDepth.getValue();
        this.evaluateFunction = EvalFunctionConfigurator.determineEvaluationFunctionName();
        this.useMvvLvaSorting = config.useMvvLvaSorting.getValue();
        this.useTTCache = config.useTTCache.getValue();
        this.useKillerMoves = config.useKillerMoves.getValue();
        this.useHistoryHeuristic = config.useHistoryHeuristic.getValue();
        this.pvSearch = config.activatePvsSearch.getValue();
        this.maxQuiescence = config.maxQuiescence.getValue();
        this.searchAlgorithm = config.searchAlgorithm.getValue().name();
        this.aspiration = config.aspiration.getValue();
        this.useNullMoves = config.useNullMoves.getValue();
        this.staticNullMove = config.staticNullMove.getValue();
        this.razoring = config.razoring.getValue();
        this.useLateMoveReductions = config.useLateMoveReductions.getValue();
        this.deltaCutoff = config.deltaCutoff.getValue();

    }

}
