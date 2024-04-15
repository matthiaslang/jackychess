package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.BoardRepresentation;

public interface EvalComponent {

    void eval(EvalResult result, BoardRepresentation bitBoard);
}
