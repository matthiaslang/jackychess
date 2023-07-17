package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;

import org.mattlang.jc.moves.MoveImpl;

/**
 * Implements Counter Move Heuristic.
 */
public class CounterMoveHeuristic {

    private final int[][][] counterMoves = new int[2][FT_ALL][NUM_BOARD_FIELDS];

    public void addCounterMove(final int color, final int parentMove, final int counterMove) {
        counterMoves[color][MoveImpl.getFigureType(parentMove)][MoveImpl.getToIndex(parentMove)] = counterMove;
    }

    public int getCounter(final int color, final int parentMove) {
        return counterMoves[color][MoveImpl.getFigureType(parentMove)][MoveImpl.getToIndex(parentMove)];
    }

    public void reset() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < FT_ALL; j++) {
                for (int k = 0; k < NUM_BOARD_FIELDS; k++) {
                    counterMoves[i][j][k] = 0;
                }
            }
        }
    }
}
