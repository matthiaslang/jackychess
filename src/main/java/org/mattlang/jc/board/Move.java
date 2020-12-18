package org.mattlang.jc.board;

public interface Move {

    Move move(BoardRepresentation board);

    byte getCapturedFigure();

    String toStr();
}
