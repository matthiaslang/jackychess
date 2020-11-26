package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.BasicMove;
import org.mattlang.jc.board.Move;

public class Engine {

    private Board board = new Board();

    private SearchMethod searchMethod;

    public Engine(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Move go() {
        return searchMethod.search(board, 4, Color.BLACK);
    }

    public Move stop() {
        return searchMethod.search(board, 1, Color.BLACK);
    }

    public void move(BasicMove move) {
        board.move(move);
    }

    public Board getBoard() {
        return board;
    }
}
