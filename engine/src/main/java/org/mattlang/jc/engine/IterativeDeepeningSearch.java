package org.mattlang.jc.engine;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public interface IterativeDeepeningSearch extends SearchMethod {

    IterativeSearchResult iterativeSearch(
            GameState gameState, GameContext gameContext, int maxDepth);

    void registerListener(IterativeDeepeningListener listener);
}
