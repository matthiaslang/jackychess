package org.mattlang.jc.engine.search;

import org.mattlang.jc.moves.MoveImpl;

public class MoveScore {

    public final int move;
    public final int score;

    public MoveScore(int move, int score) {
        this.move = move;
        this.score = score;
    }

    @Override
    public String toString() {
        return "MoveScore{" +
                new MoveImpl(move).toStr() +
                ", " + score +
                '}';
    }
}
