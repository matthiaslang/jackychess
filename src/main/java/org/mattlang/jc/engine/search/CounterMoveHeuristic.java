package org.mattlang.jc.engine.search;

import org.mattlang.jc.moves.MoveImpl;

/**
 * Implements Counter Move Heuristic.
 */
public class CounterMoveHeuristic {

    private final int[][][] counterMoves = new int[2][7][64];

    public void addCounterMove(final int color, final int parentMove, final int counterMove) {
        counterMoves[color][MoveImpl.getFigureType(parentMove)][MoveImpl.getToIndex(parentMove)] = counterMove;
    }

    public int getCounter(final int color, final int parentMove) {
        return counterMoves[color][MoveImpl.getFigureType(parentMove)][MoveImpl.getToIndex(parentMove)];
    }

    public void reset() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 64; k++) {
                    counterMoves[i][j][k] = 0;
                }
            }
        }
    }
}
