package org.mattlang.jc;

import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.evaluation.BoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.CachingEvaluateFunction;
import org.mattlang.jc.engine.evaluation.MaterialNegaMaxEval;
import org.mattlang.jc.engine.evaluation.SimpleBoardStatsGenerator;
import org.mattlang.jc.engine.search.IterativeDeepeningMtdf;
import org.mattlang.jc.movegenerator.CachingLegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

import java.util.logging.Logger;

public class SearchParameter {

    private static final Logger LOGGER = Logger.getLogger(SearchParameter.class.getSimpleName());

    private int maxDepth;

    private long timeout;

    private int maxQuiescenceDepth=5;

    public final Impl<MoveList> moveList = new Impl<>(this, () -> new BasicMoveList());

    public final Impl<EvaluateFunction> evaluateFunction = new Impl<>(this, () -> new CachingEvaluateFunction(new MaterialNegaMaxEval()));

    public final Impl<SearchMethod> searchMethod = new Impl<>(this, () -> new IterativeDeepeningMtdf());

    public final Impl<LegalMoveGenerator> legalMoveGenerator = new Impl<>(this, () -> new CachingLegalMoveGenerator());

    public final Impl<MoveGenerator> moveGenerator = new Impl<>(this, () -> new MoveGeneratorImpl2());

    public final Impl<BoardStatsGenerator> boardStatsGenerator = new Impl<>(this, () -> new SimpleBoardStatsGenerator());


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

}
