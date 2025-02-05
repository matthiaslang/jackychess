package org.mattlang.jc.engine;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public class Engine {

    private BoardRepresentation board = Configurator.createBoard();

    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    public Engine(BoardRepresentation board) {
        this.board = board;
    }

    public Engine() {
    }

    @Deprecated
    public Move go(SearchParameter searchParams) {
        return go(searchParams, new GameState(board, null), new GameContext());
    }

    public Move go(SearchParameter searchParams, GameState gameState, GameContext gameContext) {
        IterativeDeepeningSearch searchMethod = searchParams.searchMethod.instance();
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(searchParams, gameState, gameContext).getSavedMove();
    }

    public IterativeSearchResult goIterative(SearchParameter searchParams, GameState gameState,
            GameContext gameContext) {
        IterativeDeepeningSearch searchMethod = searchParams.searchMethod.instance();
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(searchParams, gameState, gameContext);
    }

    public BoardRepresentation getBoard() {
        return board;
    }

    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = listener;
    }

}
