package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.IterativeDeepeningListener;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.uci.GameContext;

public class Engine implements IterativeDeepeningListener {

    private BoardRepresentation board = Factory.getDefaults().boards.create();

    private IterativeDeepeningSearch searchMethod = Factory.getDefaults().searchMethod.create();

    private int depth = Factory.getDefaults().getConfig().maxDepth.getValue();

    private Move bestMoveSoFar = null;

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
        searchMethod.registerListener(this);
        return searchMethod.search(new GameState(board, null), new GameContext(), depth);
    }

    public Move go(GameState gameState, GameContext gameContext) {
        searchMethod.registerListener(this);
        return searchMethod.iterativeSearch(gameState, gameContext, depth).getSavedMove();
    }

    public Move go(GameState gameState, GameContext gameContext, int depth) {
        searchMethod.registerListener(this);
        return searchMethod.iterativeSearch(gameState, gameContext, depth).getSavedMove();
    }

    public IterativeSearchResult goIterative(GameState gameState, GameContext gameContext) {
        searchMethod.registerListener(this);
        return searchMethod.iterativeSearch(gameState, gameContext, depth);
    }
    //
    //    public void move(Move move) {
    //        board.move(move);
    //    }

    public BoardRepresentation getBoard() {
        return board;
    }

    public Move getBestMoveSoFar() {
        return bestMoveSoFar;
    }

    @Override
    public void updateBestRoundMove(Move bestMove) {
        bestMoveSoFar = bestMove;
    }

}
