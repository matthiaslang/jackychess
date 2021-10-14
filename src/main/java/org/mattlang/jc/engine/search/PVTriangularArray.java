package org.mattlang.jc.engine.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Triangular Array to store the PVs during recursive search.
 */
public class PVTriangularArray {

    private static final int MAX = 100;

    /**
     * Moves of the triangular array.
     */
    private int[][] array = new int[MAX][MAX];

    /**
     * Sets a PV found in ply ply.
     * Updates the internal triangular array.
     *
     * @param bestMove
     * @param ply
     */
    public void set(int bestMove, int ply) {
        int index = ply - 1;
        int[] rowForPly = array[index];
        int[] rowForUnderPly = array[index + 1];

        rowForPly[0] = bestMove;
        for (int i = 0; i < MAX - 1; i++) {
            if (rowForUnderPly[i] != 0) {
                rowForPly[i + 1] = rowForUnderPly[i];
            } else {
                break;
            }
        }
    }

    public List<Integer> getPvMoves() {
        ArrayList<Integer> result = new ArrayList();
        for (int i = 0; i < MAX; i++) {
            int pv = array[0][i];
            if (pv != 0) {
                result.add(pv);
            } else {
                break;
            }
        }
        return result;
    }

    public void reset() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                array[i][j] = 0;
            }
        }
    }
}
