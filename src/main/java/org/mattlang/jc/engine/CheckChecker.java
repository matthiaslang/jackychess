package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

/**
 * Checks if the king is in check.
 *
 */
public interface CheckChecker {

    /**
     * Is the other colors king in check?
     *
     * @param otherColor
     * @return
     */
    boolean isInChess(BoardRepresentation board, Color otherColor);
}
