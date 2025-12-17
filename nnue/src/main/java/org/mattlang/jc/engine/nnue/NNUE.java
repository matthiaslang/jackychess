package org.mattlang.jc.engine.nnue;


import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.TuningCache;
import org.mattlang.jc.engine.search.SearchThreadContextCache;

/**
 * Placeholder eval function.
 */
public class NNUE implements EvaluateFunction {

    @Override
    public void init(BoardRepresentation board) {
    }

    @Override
    public void doMove(BoardRepresentation board, Move move) {
    }

    @Override
    public int eval(BoardRepresentation currBoard, int who2Move) {
        return 0;
    }

    @Override
    public void undoMove(BoardRepresentation board, Move currMoveObj) {

    }

    @Override
    public void associateThreadCache(SearchThreadContextCache cache) {

    }

    @Override
    public TuningCache getTuningCache() {
        return null; // this evalfunction must not be tuned in a conventional way
    }
}
