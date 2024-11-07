package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public class Engine {

    private BoardRepresentation board = Configurator.createBoard();

    private IterativeDeepeningSearch searchMethod = Factory.getDefaults().searchMethod.create();

    private int depth = Factory.getDefaults().getConfig().maxDepth.getValue();

    private IterativeDeepeningListener listener = IterativeDeepeningPVS.NOOP_LISTENER;

    public Engine(BoardRepresentation board) {
        this.board = board;
    }

    public Engine(BoardRepresentation board, int depth) {
        this.board = board;
        this.depth = depth;
    }

    public Engine() {
    }

    public Engine(IterativeDeepeningSearch searchMethod, int depth) {
        this.searchMethod = searchMethod;
        this.depth = depth;
    }

    @Deprecated
    public Move go() {
        searchMethod.registerListener(listener);
        return searchMethod.search(new GameState(board, null), new GameContext(), depth);
    }

    public Move go(GameState gameState, GameContext gameContext) {
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(gameState, gameContext, depth).getSavedMove();
    }

    public Move go(GameState gameState, GameContext gameContext, int depth) {
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(gameState, gameContext, depth).getSavedMove();
    }

    public IterativeSearchResult goIterative(GameState gameState, GameContext gameContext) {
        searchMethod.registerListener(listener);
        return searchMethod.iterativeSearch(gameState, gameContext, depth);
    }

    public BoardRepresentation getBoard() {
        return board;
    }

    public void registerListener(IterativeDeepeningListener listener) {
        this.listener = listener;
    }

}
