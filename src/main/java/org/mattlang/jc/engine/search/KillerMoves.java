package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Color;

/**
 * Holds the found killer moves used for move sorting.
 */
public class KillerMoves {

    int MAX_KILLERS = 2;

    int MAX_PLY = 100;

    private int[][] killerMovesWhite = new int[MAX_PLY][2];
    private int[][] killerMovesBlack = new int[MAX_PLY][2];

    public void addKiller(Color color, int move, int ply) {
        if (ply < MAX_PLY) {
            int[] kmovesList = getOrCreateKillerList(color, ply);
            if (kmovesList[0] != move && kmovesList[1] != move) {
                kmovesList[1] = kmovesList[0];
                kmovesList[0] = move;
            }
        }
    }

    public boolean isKiller(Color color, int moveInt, int ply) {
        if (ply > MAX_PLY) {
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

    private int[] getOrCreateKillerList(Color color, int ply) {
        int[][] killerMoves = color == Color.WHITE ? killerMovesWhite : killerMovesBlack;
        int[] kmovesList = killerMoves[ply];
        //        if (kmovesList == null) {
        //            kmovesList = new ArrayList<>();
        //            killerMoves[ply] = kmovesList;
        //        }
        return kmovesList;
    }
}
