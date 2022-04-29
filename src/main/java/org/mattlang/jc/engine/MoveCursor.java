package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

/**
 * Lightweight cursor-based class for Iterator-based access to moves of a move list.
 */
public interface MoveCursor extends Move {

    void move(BoardRepresentation board);

    void undoMove(BoardRepresentation board);

    int getOrder();

    void next();

    boolean hasNext();
}
