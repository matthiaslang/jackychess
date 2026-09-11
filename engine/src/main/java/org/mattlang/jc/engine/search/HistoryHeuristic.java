package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Move;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;

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

    public void update(int color, Move move, int depth) {
        updateHist(color, move, calcBonus(depth));
    }

    private void updateHist(int colorIdx, Move move, int bonus) {
        int existingVal = posHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
        posHistory[colorIdx][move.getFromIndex()][move.getToIndex()] =
                calcNewVal(existingVal, bonus);
    }

    public int calcValue(Move move, int colorIdx) {
        return posHistory[colorIdx][move.getFromIndex()][move.getToIndex()];
    }

    public void updateBad(int color, Move move, int depth) {
        updateHist(color, move, -calcBonus(depth));
    }

}
