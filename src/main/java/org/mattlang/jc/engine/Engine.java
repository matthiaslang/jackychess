package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.uci.GameContext;

public class Engine {

    private BoardRepresentation board = Factory.getDefaults().boards.create();

    private SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

    private int depth = Factory.getDefaults().getConfig().maxDepth.getValue();

    public Engine(BoardRepresentation board) {
        this.board = board;
    }
    public Engine(BoardRepresentation board, int depth) {
        this.board = board;
        this.depth = depth;
    }

    public Engine() {
    }

    public Engine(SearchMethod searchMethod, int depth) {
        this.searchMethod = searchMethod;
        this.depth = depth;
    }

    @Deprecated
    public Move go() {
        return searchMethod.search(new GameState(board, Color.BLACK, new SimpleRepetitionChecker()), new GameContext(), depth);
    }

    @Deprecated
    public Move go(Color color) {
        return searchMethod.search(new GameState(board, color, new SimpleRepetitionChecker()), new GameContext(), depth);
    }

    public Move go(GameState gameState, GameContext gameContext) {
        return searchMethod.search(gameState, gameContext, depth);
    }

    public void move(Move move) {
        board.move(move);
    }

    public BoardRepresentation getBoard() {
        return board;
    }
}
