package org.mattlang.jc.engine;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BasicMove;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public class Engine {

    private Board board = new Board();

    private SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

    private int depth = Factory.getDefaults().getMaxDepth();

    public Engine(Board board) {
        this.board = board;
    }
    public Engine(Board board, int depth) {
        this.board = board;
        this.depth = depth;
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

    public Move go(Color color) {
        return searchMethod.search(board, depth, color);
    }

    public void move(BasicMove move) {
        board.move(move);
    }

    public Board getBoard() {
        return board;
    }
}
