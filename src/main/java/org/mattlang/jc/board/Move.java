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

    boolean isEnPassant();

    boolean isCastling();

    boolean isPromotion();

    Figure getPromotedFigure();

    void setOrder(int order);

    int getOrder();


    /** Representation of the move as int value. */
    int toInt();
}
