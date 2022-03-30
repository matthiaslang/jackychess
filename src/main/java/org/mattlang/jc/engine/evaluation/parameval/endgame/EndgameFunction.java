package org.mattlang.jc.engine.evaluation.parameval.endgame;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

public interface EndgameFunction {

    int evaluate(BoardRepresentation board, int stronger, int weaker, ParameterizedMaterialEvaluation matEvaluation);
}
