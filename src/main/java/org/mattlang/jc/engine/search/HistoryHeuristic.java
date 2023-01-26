package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class HistoryHeuristic {

    private int[][][] posHistory = new int[2][64][64];

    // the "malus" history
    private int[][][] badHistory = new int[2][64][64];

    public HistoryHeuristic() {
        // init with 1 to save divide operation
        init(posHistory);
        init(badHistory);
    }

    private void init(int[][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    histArray[i][j][k] = 1;
                }
            }
        }
    }

    public void update(Color color, MoveCursor move, int depth) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        posHistory[colorIdx][move.getFromIndex()][move.getToIndex()] += depth * depth;
    }

    public int calcValue(Move move, Color color) {
        int colorIdx = color == Color.WHITE ? 0 : 1;

        return 100 * posHistory[colorIdx][move.getFromIndex()][move.getToIndex()]
                / badHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
    }

    public void updateBad(Color color, MoveCursor move, int depth) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        badHistory[colorIdx][move.getFromIndex()][move.getToIndex()] += depth * depth;
    }
}
