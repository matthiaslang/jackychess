package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class HistoryHeuristic extends AbstractHistory {

    private int[][][] posHistory = new int[2][NUM_BOARD_FIELDS][NUM_BOARD_FIELDS];

    public HistoryHeuristic() {
        // init with 1 to save divide operation
        reset();
    }

    public void reset() {
        init(posHistory);
    }

    private void init(int[][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < NUM_BOARD_FIELDS; j++) {
                for (int k = 0; k < NUM_BOARD_FIELDS; k++) {
                    histArray[i][j][k] = 0;
                }
            }
        }
    }

    public void update(int color, MoveCursor move, int depth) {
        updateHist(color, move, calcBonus(depth));
    }

    private void updateHist(int colorIdx, MoveCursor move, int bonus) {
        int clampedBonus = clamp(bonus, -HIST_MAX, HIST_MAX);
        int existingVal = posHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
        posHistory[colorIdx][move.getFromIndex()][move.getToIndex()] +=
                clampedBonus - existingVal * Math.abs(clampedBonus) / HIST_MAX;
    }

    public int calcValue(Move move, int colorIdx) {
        return 100 * posHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
    }

    public void updateBad(int color, MoveCursor move, int depth) {
        updateHist(color, move, -calcBonus(depth));
    }

}
