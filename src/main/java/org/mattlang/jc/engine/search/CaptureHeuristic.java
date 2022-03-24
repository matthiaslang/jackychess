package org.mattlang.jc.engine.search;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;

public class CaptureHeuristic {

    private int[][][][] posHistory = new int[2][6][6][64];

    // the "malus" history
    private int[][][][] badHistory = new int[2][6][6][64];

    public CaptureHeuristic() {
        // init with 1 to save divide operation
        init(posHistory);
        init(badHistory);
    }

    private void init(int[][][][] histArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 6; k++) {
                    for (int l = 0; l < 64; l++) {
                        histArray[i][j][k][l] = 1;
                    }
                }
            }
        }
    }

    public void update(Color color, MoveCursor move, int depth) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        int fig = move.getFigureType();
        int captureFig = (move.getCapturedFigure() & MASK_OUT_COLOR);
        int historyChange = depth * depth + 5 * depth - 2;

        posHistory[colorIdx][fig][captureFig][move.getToIndex()] += historyChange;
    }

    public int calcValue(Move move, Color color) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        int fig = move.getFigureType();
        int captureFig = (move.getCapturedFigure() & MASK_OUT_COLOR);

        return posHistory[colorIdx][fig][captureFig][move.getToIndex()]
                / badHistory[colorIdx][fig][captureFig][move.getToIndex()];
    }

    public void updateBad(Color color, MoveCursor move, int depth) {
//        int colorIdx = color == Color.WHITE ? 0 : 1;
//        int fig = move.getFigureType();
//        int captureFig = (move.getCapturedFigure() & MASK_OUT_COLOR);
//        int historyChange = depth * depth + 5 * depth - 2;
//        badHistory[colorIdx][fig][captureFig][move.getToIndex()] += historyChange;

    }
}
