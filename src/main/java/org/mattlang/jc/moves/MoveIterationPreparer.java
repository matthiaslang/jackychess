package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.movegenerator.MoveGenerator;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 */
public interface MoveIterationPreparer {

    /**
     * prepares a new move iteration.
     *
     * @param stc
     * @param mode
     * @param board
     * @param color
     * @param ply
     * @param hashMove
     * @param parentMove
     */
    void prepare(SearchThreadContext stc, MoveGenerator.GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove);

    /**
     * Returns a move board iterator for iteration over legal moves on the board.
     * There must be a prepare call before
     * @return
     */
    MoveBoardIterator iterateMoves();
}
