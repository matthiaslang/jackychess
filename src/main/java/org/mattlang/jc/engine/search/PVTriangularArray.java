package org.mattlang.jc.engine.search;


import static org.mattlang.jc.Constants.MAX_PLY;

import java.util.ArrayList;
import java.util.List;

/**
 * Triangular Array to store the PVs during recursive search.
 */
public class PVTriangularArray {

    /**
     * Moves of the triangular array.
     */
    private int[][] array = new int[MAX_PLY][MAX_PLY];

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
        // copy over the next deeper ply. if we reach a 0 value, we stop ( but after copying the 0 value as end marker).
        for (int i = 0; i < MAX_PLY - 1; i++) {
            rowForPly[i + 1] = rowForUnderPly[i];
            if (rowForUnderPly[i] == 0) {
                break;
            }
        }
    }

    public List<Integer> getPvMoves() {
        ArrayList<Integer> result = new ArrayList();
        for (int i = 0; i < MAX_PLY; i++) {
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
        for (int i = 0; i < MAX_PLY; i++) {
            for (int j = 0; j < MAX_PLY; j++) {
                array[i][j] = 0;
            }
        }
    }

    /**
     * reset pv ply info.
     *
     * This is necessary to not deliver illegal moves after "check" situations to use "older" pv infos from a previous
     * deeper search.
     *
     * @param ply
     */
    public void reset(int ply) {
        int index = ply - 1;
        int[] rowForPly = array[index];
        // mark as empty:
        rowForPly[0] = 0;
    }
}
