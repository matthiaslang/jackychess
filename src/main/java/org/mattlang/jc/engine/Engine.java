package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public class Engine {

    private Board board = new Board();

    private SearchMethod searchMethod;

    public Engine(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Move go() {
        return searchMethod.search(board, 20, Color.BLACK);
    }

    public Move stop() {
        return searchMethod.search(board, 1, Color.BLACK);
    }

    public void setStartPosition() {
        board.setStartPosition();
    }

    public void move(Move move) {
        board.move(move);
    }

    public void clearPosition() {
        board.clearPosition();
    }

    public void setPosition(String[] fenPosition) {
        board.setPosition(fenPosition);
    }
}
