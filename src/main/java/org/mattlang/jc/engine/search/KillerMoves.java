package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

/**
 * Holds the found killer moves used for move sorting.
 */
public class KillerMoves {

    public static final int MAX_KILLERS = 2;

    // killers for all plies (+1 for resetting)
    private int[][] killers = new int[MAX_PLY_INDEX + 1][MAX_KILLERS];

    public void addKiller(int move, int ply) {
        if (ply < MAX_PLY_INDEX) {
            int[] kmovesList = getOrCreateKillerList(ply);
            if (kmovesList[0] != move && kmovesList[1] != move) {
                kmovesList[1] = kmovesList[0];
                kmovesList[0] = move;
            }
        }
    }

    public boolean isKiller(int moveInt, int ply) {
        if (ply > MAX_PLY_INDEX) {
            return false;
        }
        int[] kmovesList = getOrCreateKillerList(ply);
        for (int i = 0; i < kmovesList.length; i++) {
            if (kmovesList[i] == moveInt) {
                return true;
            }
        }
        return false;
    }

    public int[] getOrCreateKillerList(int ply) {
        return killers[ply];
    }

    public void reset() {
        for (int i = 0; i < MAX_PLY_INDEX; i++) {
            for (int j = 0; j < MAX_KILLERS; j++) {
                killers[i][j] = 0;
            }
        }
    }

    public void resetAtPly(int ply) {
        for (int j = 0; j < MAX_KILLERS; j++) {
            killers[ply][j] = 0;
        }
    }
}
