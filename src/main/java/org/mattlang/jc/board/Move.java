package org.mattlang.jc.board;

public interface Move {

    void move(BoardRepresentation board);

    void undo(BoardRepresentation board);

    byte getCapturedFigure();

    byte getFigureType();

    String toStr();
}
