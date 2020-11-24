package org.mattlang.jc.engine;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;

public class Engine {

    private Board board = new Board();

    public void go() {

    }

    public void stop() {

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
