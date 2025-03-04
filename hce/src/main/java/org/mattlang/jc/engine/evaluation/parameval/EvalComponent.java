package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.BoardRepresentation;

public interface EvalComponent {

    /**
     * Evaluates this component and returns the mg/eg combined value.
     *
     * @param result
     * @param bitBoard
     * @return
     */
    int eval(EvalResult result, BoardRepresentation bitBoard);
}
