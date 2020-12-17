package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Move;

public class MoveScore {
    public final Move move;
    public final int score;

    public MoveScore(Move move, int score) {
        this.move = move;
        this.score = score;
    }
}
