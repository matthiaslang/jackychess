package org.mattlang.jc.engine;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public class Engine {

    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    public Engine() {
    }

    public Move go(SearchParameter searchParams, GameState gameState) {
        return go(searchParams, gameState, new GameContext());
    }

    public Move go(SearchParameter searchParams, GameState gameState, GameContext gameContext) {
        return goIterative(searchParams, gameState, gameContext).getSavedMove();
    }

    public IterativeSearchResult goIterative(SearchParameter searchParams, GameState gameState,
            GameContext gameContext) {
        IterativeDeepeningSearch searchMethod = searchParams.getIterativeDeepeningSearch();
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(searchParams, gameState, gameContext);
    }

    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = listener;
    }

}
