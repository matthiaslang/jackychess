package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;

public class Engine {

    private BoardRepresentation board = new Board();

    private SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

    private int depth = Factory.getDefaults().getMaxDepth();

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
        return searchMethod.search(new GameState(board, Color.BLACK, new SimpleRepetitionChecker()), depth);
    }

    @Deprecated
    public Move go(Color color) {
        return searchMethod.search(new GameState(board, color, new SimpleRepetitionChecker()), depth);
    }

    public Move go(GameState gameState) {
        return searchMethod.search(gameState, depth);
    }

    public void move(BasicMove move) {
        board.move(move);
    }

    public BoardRepresentation getBoard() {
        return board;
    }
}
