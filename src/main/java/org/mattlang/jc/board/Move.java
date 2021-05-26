package org.mattlang.jc.board;

public interface Move {

    void move(BoardRepresentation board);

    void undo(BoardRepresentation board);

    byte getCapturedFigure();

    boolean isCapture();

    byte getFigureType();

    int getFromIndex();

    int getToIndex();

    String toStr();

    void setOrder(int order);

    int getOrder();

    short getMvvLva();

}
