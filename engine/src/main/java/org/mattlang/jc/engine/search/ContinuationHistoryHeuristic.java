package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.NUM_BOARD_FIELDS;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.moves.MoveImpl;

public class ContinuationHistoryHeuristic extends AbstractHistory{

    private int[][][][][] posHistory = new int[2][FT_ALL][NUM_BOARD_FIELDS][FT_ALL][NUM_BOARD_FIELDS];

    public ContinuationHistoryHeuristic() {
        reset();
    }

    public void reset() {
        init(posHistory);
    }

    private void init(int[][][][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < FT_ALL; j++) {
                for (int k = 0; k < NUM_BOARD_FIELDS; k++) {
                    for (int l = 0; l < FT_ALL; l++) {
                        for (int m = 0; m < NUM_BOARD_FIELDS; m++) {
                            histArray[i][j][k][l][m] = 0;
                        }
                    }
                }
            }
        }
    }

    public void update(int color, int prevMove, MoveCursor move, int depth) {
        updateHist(color, prevMove, move, calcBonus(depth));
    }


    private void updateHist(int colorIdx, int prevMove, MoveCursor move, int bonus) {
        if (prevMove == 0) {
            return;
        }

        int clampedBonus = clamp(bonus, -HIST_MAX, HIST_MAX);

        int existingVal=posHistory[colorIdx][MoveImpl.getFigureType(prevMove)][MoveImpl.getToIndex(
                prevMove)][move.getFigureType()][move.getToIndex()];
        posHistory[colorIdx][MoveImpl.getFigureType(prevMove)][MoveImpl.getToIndex(
                prevMove)][move.getFigureType()][move.getToIndex()] +=  clampedBonus - existingVal * Math.abs(clampedBonus) / HIST_MAX;
    }

    public int calcValue(int prevMove, Move move, int colorIdx) {
        if (prevMove == 0) {
            return 0;
        }
        return posHistory[colorIdx][MoveImpl.getFigureType(prevMove)][MoveImpl.getToIndex(
                prevMove)][move.getFigureType()][move.getToIndex()];
    }

    public void updateBad(int color, int prevMove, MoveCursor move, int depth) {
        updateHist(color, prevMove, move, -calcBonus(depth));
    }

}