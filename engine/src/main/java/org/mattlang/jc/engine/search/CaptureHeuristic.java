package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

public class CaptureHeuristic extends AbstractHistory {

    private int[][][][] posHistory = new int[2][FT_ALL][FT_ALL][NUM_BOARD_FIELDS];

    public CaptureHeuristic() {
        // init with 1 to save divide operation
        reset();
    }

    public void reset() {
        init(posHistory);
    }

    private void init(int[][][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < FT_ALL; j++) {
                for (int j2 = 0; j2 < FT_ALL; j2++) {
                    for (int k = 0; k < NUM_BOARD_FIELDS; k++) {
                        histArray[i][j][j2][k] = 0;
                    }
                }
            }
        }
    }

    public void update(Color color, MoveCursor move, int depth) {
        updateHist(color, move, calcBonus(depth));
    }

    private void updateHist(Color color, MoveCursor move, int bonus) {
        int colorIdx = color.ordinal();
        byte captFigType = (byte) (move.getCapturedFigure() & MASK_OUT_COLOR);
        int clampedBonus = clamp(bonus, -HIST_MAX, HIST_MAX);
        int existingVal = posHistory[colorIdx][move.getFigureType()][captFigType][move.getToIndex()];

        posHistory[colorIdx][move.getFigureType()][captFigType][move.getToIndex()] +=
                clampedBonus - existingVal * Math.abs(clampedBonus) / HIST_MAX;
    }

    public int calcValue(Move move, Color color) {
        int colorIdx = color.ordinal();
        byte captFigType = (byte) (move.getCapturedFigure() & MASK_OUT_COLOR);
        return posHistory[colorIdx][move.getFigureType()][captFigType][move.getToIndex()];
    }

    public void updateBad(Color color, MoveCursor move, int depth) {
        updateHist(color, move, -calcBonus(depth));
    }

}