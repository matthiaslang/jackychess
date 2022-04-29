package org.mattlang.jc.engine.search;

import org.mattlang.jc.moves.MoveImpl;

public class MoveScore {

    public final int move;
    public final int score;

    public final int numSearchedNodes;

    public MoveScore(int move, int score, int numSearchedNodes) {
        this.move = move;
        this.score = score;
        this.numSearchedNodes = numSearchedNodes;
    }

    @Override
    public String toString() {
        return new MoveImpl(move).toStr() +
                ": " + score;
    }
}
