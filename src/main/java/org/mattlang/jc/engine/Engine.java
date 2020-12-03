package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BasicMove;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public class Engine {

    private Board board = new Board();

    private SearchMethod searchMethod = Factory.createSearchMethod();

    private int depth = 6;

    public Engine(Board board) {
        this.board = board;
    }

    public Engine() {
    }

    public Engine(SearchMethod searchMethod, int depth) {
        this.searchMethod = searchMethod;
        this.depth = depth;
    }

    public Move go() {
        return searchMethod.search(board, depth, Color.BLACK);
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
