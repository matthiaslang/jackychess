package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;

/**
 * Lightweight cursor-based class for Iterator-based access to moves of a move list.
 */
public interface MoveCursor {

    void move(BoardRepresentation board);

    int getMoveInt();

    int getOrder();

    void undoMove(BoardRepresentation board);

    boolean isCapture();

    boolean isPawnPromotion();

    boolean isEnPassant();

    boolean isCastling();

    byte getCapturedFigure();

    byte getFigureType();

    int getFromIndex();

    int getToIndex();

    void remove();
}
