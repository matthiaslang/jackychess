package org.mattlang.jc;

import static java.util.stream.Collectors.joining;

import java.util.Map;

import org.mattlang.jc.engine.search.IterativeSearchResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BenchmarkIterativeResults extends BenchmarkResults<IterativeSearchResult> {

    private String move;

    private int nodesVisited;
    private int quiescenceNodesVisited;

    public BenchmarkIterativeResults(String name,
            ExecResults<IterativeSearchResult> execResults, Map stats, String fenposition) {
        super(name, execResults, stats, fenposition);

        move = execResults.getResults().stream().map(r -> r.getSavedMove().toStr()).collect(joining());
        nodesVisited = execResults.getResults().stream().map(r -> r.getRslt().nodesVisited).reduce(0, Integer::sum);
        quiescenceNodesVisited = execResults.getResults()
                .stream()
                .map(r -> r.getRslt().quiescenceNodesVisited)
                .reduce(0, Integer::sum);
    }
}
