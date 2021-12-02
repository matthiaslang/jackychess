package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;

public interface EvalComponent {

    void eval(EvalResult result, BitBoard bitBoard, Color who2Move);
}
