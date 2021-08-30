package org.mattlang.jc.engine.see;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

public class SEE {

    /**
     * Calculates the statice exchange evaluation of a capturing move.
     *
     * Evaluates statically all re-captures on the target square of that move.
     *
     * @param board
     * @param move
     * @return
     */
    public int see(BoardRepresentation board, Move move) {
        int targetSquare = move.getToIndex();

//        createWhiteRecaptures(board, targetSquare);


        return 0;
    }

}
