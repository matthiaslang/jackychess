package org.mattlang.jc.engine.search;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.Move;

/**
 * Triangular Array to store the PVs during recursive search.
 */
public class PVTriangularArray {

    private static final int MAX=100;

    private Move[][] array = new Move[MAX][MAX];

    /**
     * Sets a PV found in ply ply.
     * Updates the internal triangular array.
     *
     * @param move
     * @param ply
     */
    public void set(Move move, int ply) {
        int index = ply - 1;
        Move[] rowForPly = array[index];
        Move[] rowForUnderPly = array[index + 1];

        rowForPly[0] = move;
        for (int i = 0; i < MAX-1; i++) {
            if (rowForUnderPly[i] != null) {
                rowForPly[i + 1] = rowForUnderPly[i];
            } else {
                break;
            }
        }
    }

    public List<Move> getPvMoves() {
        ArrayList<Move> result = new ArrayList();
        for (int i = 0; i < MAX; i++) {
            Move pv = array[0][i];
            if (pv != null) {
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
                array[i][j] = null;
            }
        }
    }
}
