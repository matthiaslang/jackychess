package org.mattlang.jc.engine;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;

public interface AlphaBetaSearchMethod extends SearchMethod, StatisticsCollector {

    void reset();

    void resetCaches();

    int getNodesVisited();

    NegaMaxResult searchWithScore(SearchThreadContext stc, GameState gameState, GameContext context, int currdepth,
            int alphaStart, int betaStart, long stopTime, OrderHints orderHints);

}
