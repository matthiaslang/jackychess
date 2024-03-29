package org.mattlang.jc.engine.search;


import org.mattlang.jc.board.Color;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

/**
 * Holds the found killer moves used for move sorting.
 */
public class KillerMoves {

    public static final int MAX_KILLERS = 2;

    private int[][] killerMovesWhite = new int[MAX_PLY_INDEX][MAX_KILLERS];
    private int[][] killerMovesBlack = new int[MAX_PLY_INDEX][MAX_KILLERS];

    public void addKiller(Color color, int move, int ply) {
        if (ply < MAX_PLY_INDEX) {
            int[] kmovesList = getOrCreateKillerList(color, ply);
            if (kmovesList[0] != move && kmovesList[1] != move) {
                kmovesList[1] = kmovesList[0];
                kmovesList[0] = move;
            }
        }
    }

    public boolean isKiller(Color color, int moveInt, int ply) {
        if (ply > MAX_PLY_INDEX) {
            return false;
        }
        int[] kmovesList = getOrCreateKillerList(color, ply);
        for (int i = 0; i < kmovesList.length; i++) {
            if (kmovesList[i] == moveInt) {
                return true;
            }
        }
        return false;
    }

    public int[] getOrCreateKillerList(Color color, int ply) {
        int[][] killerMoves = color == Color.WHITE ? killerMovesWhite : killerMovesBlack;
        return killerMoves[ply];
    }

    public void reset() {
        for (int i = 0; i < MAX_PLY_INDEX; i++) {
            for (int j = 0; j < MAX_KILLERS; j++) {
                killerMovesWhite[i][j] = 0;
                killerMovesBlack[i][j] = 0;
            }
        }
    }
}
