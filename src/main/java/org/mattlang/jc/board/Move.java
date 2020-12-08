package org.mattlang.jc.board;

public interface Move {

    Move move(Board board);


    byte getCapturedFigure();

    String toStr();
}
