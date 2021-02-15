package org.mattlang.jc.engine;

import org.mattlang.jc.StatisticsCollector;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.sorting.OrderHints;

public interface AlphaBetaSearchMethod extends SearchMethod, StatisticsCollector {

    void reset();

    void resetCaches();

    Move getSavedMove();

    int getNodesVisited();

    int getNodes();

    NegaMaxResult searchWithScore(GameState gameState, int currdepth, int alphaStart, int betaStart, long stopTime, OrderHints orderHints);

    int getSavedMoveScore();
}
