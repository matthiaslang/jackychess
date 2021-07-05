package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

/**
 * Lightweight cursor-based class for Iterator-based access to moves of a move list.
 */
public interface MoveCursor {

    void move(BoardRepresentation board);

    Move getMove();

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
