package org.mattlang.jc.engine.evaluation.parameval.endgame;

import org.mattlang.jc.board.BoardRepresentation;

public interface EndgameFunction {

    int evaluate(BoardRepresentation board, int stronger, int weaker, int materialEval);
}
