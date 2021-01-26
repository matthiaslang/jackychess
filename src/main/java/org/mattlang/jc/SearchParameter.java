package org.mattlang.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.*;
import org.mattlang.jc.engine.evaluation.*;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

public class SearchParameter {

    private static final Logger LOGGER = Logger.getLogger(SearchParameter.class.getSimpleName());

    private List<Impl> impls = new ArrayList<>();

    private int maxDepth;

    private long timeout;

    private int maxQuiescenceDepth = 5;

    public final Impl<MoveList> moveList = new Impl<>(this, () -> new BasicMoveList());

    public final Impl<BoardRepresentation> boards = new Impl<>(this, () -> new Board2());

    public final Impl<EvaluateFunction> evaluateFunction = new Impl<>(this, () -> new CachingEvaluateFunction(new MaterialNegaMaxEval()));

    public final Impl<SearchMethod> searchMethod = new Impl<>(this, () -> new IterativeDeepeningMtdf());

    public final Impl<LegalMoveGenerator> legalMoveGenerator = new Impl<>(this, () -> new LegalMoveGeneratorImpl3());

    public final Impl<MoveGenerator> moveGenerator = new Impl<>(this, () -> new MoveGeneratorImpl2());

    public final Impl<BoardStatsGenerator> boardStatsGenerator = new Impl<>(this, () -> new SimpleBoardStatsGenerator());

    public final Impl<StalemateChecker> stalemateChecker = new Impl<>(this, StalemateCheckerImpl::new);

    public SearchParameter setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public SearchParameter setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public SearchParameter setMaxQuiescenceDepth(int maxQuiescenceDepth) {
        this.maxQuiescenceDepth = maxQuiescenceDepth;
        return this;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getTimeout() {
        return timeout;
    }

    public int getMaxQuiescenceDepth() {
        return maxQuiescenceDepth;
    }

    public void log() {
        UCILogger.log("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        UCILogger.log("Evaluation: " + evaluateFunction.instance().getClass().getSimpleName());
        UCILogger.log("Max Depth: " + getMaxDepth());
        UCILogger.log("Max Quiescence Depth: " + getMaxQuiescenceDepth());
        UCILogger.log("Timeout ms: " + getTimeout());

        LOGGER.info("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        LOGGER.info("Evaluation: " + evaluateFunction.instance().getClass().getSimpleName());
        LOGGER.info("Max Depth: " + getMaxDepth());
        LOGGER.info("Timeout ms: " + getTimeout());
        LOGGER.info("Max Quiescence Depth: " + getMaxQuiescenceDepth());

    }

    public Map collectStatistics() {
        HashMap stats = new HashMap();
        for (Impl impl : impls) {
            impl.collectStatistics(stats);
        }
        return stats;
    }

    public <T> void register(Impl impl) {
        impls.add(impl);
    }

    public static void printStats(Map stats) {
        printStats("", stats);
    }

    public void printStats() {
        printStats(collectStatistics());
    }

    public static void printStats(String prefix, Map stats) {
        StringBuilder b = new StringBuilder();
        stats.forEach((key, value) -> {
            if (value instanceof Map) {
                printStats(prefix + "." + key.toString(), (Map) value);
            } else {
                if (b.length() > 0) {
                    b.append(", ");
                }
                b.append(key + ":" + value);
            }
        });
        if (b.length() > 0) {
            System.out.println(prefix + ": " + b.toString());
        }
    }
}
