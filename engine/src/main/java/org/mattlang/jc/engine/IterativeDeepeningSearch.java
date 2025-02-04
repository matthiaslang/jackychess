package org.mattlang.jc.engine;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public interface IterativeDeepeningSearch extends SearchMethod {

    IterativeSearchResult iterativeSearch(SearchParameter searchParams,
            GameState gameState, GameContext gameContext);

    void registerListener(IterativeDeepeningListener listener);
}
