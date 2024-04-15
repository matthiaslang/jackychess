package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class HistoryHeuristic {

    private int[][][] posHistory = new int[2][NUM_BOARD_FIELDS][NUM_BOARD_FIELDS];

    // the "malus" history
    private int[][][] badHistory = new int[2][NUM_BOARD_FIELDS][NUM_BOARD_FIELDS];

    public HistoryHeuristic() {
        // init with 1 to save divide operation
        reset();
    }

    public void reset() {
        init(posHistory);
        init(badHistory);
    }

    private void init(int[][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < NUM_BOARD_FIELDS; j++) {
                for (int k = 0; k < NUM_BOARD_FIELDS; k++) {
                    histArray[i][j][k] = 1;
                }
            }
        }
    }

    public void update(Color color, MoveCursor move, int depth) {
        int colorIdx = color.ordinal();
        posHistory[colorIdx][move.getFromIndex()][move.getToIndex()] += depth * depth;
    }

    public int calcValue(Move move, Color color) {
        int colorIdx = color.ordinal();

        return 100 * posHistory[colorIdx][move.getFromIndex()][move.getToIndex()]
                / badHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
    }

    public void updateBad(Color color, MoveCursor move, int depth) {
        int colorIdx = color.ordinal();
        badHistory[colorIdx][move.getFromIndex()][move.getToIndex()] += depth * depth;
    }

}
