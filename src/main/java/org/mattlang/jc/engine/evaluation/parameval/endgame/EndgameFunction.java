package org.mattlang.jc.engine.evaluation.parameval.endgame;

import org.mattlang.jc.board.bitboard.BitBoard;

public interface EndgameFunction {

    int evaluate(BitBoard bitBoard, int stronger, int weaker);
}
