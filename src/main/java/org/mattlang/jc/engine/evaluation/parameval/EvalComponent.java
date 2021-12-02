package org.mattlang.jc.engine.evaluation.parameval;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;

public interface EvalComponent {

    int eval(BitBoard bitBoard, Color who2Move);
}
