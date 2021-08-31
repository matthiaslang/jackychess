package org.mattlang.jc.engine.search;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;

/**
 * Holds the found killer moves used for move sorting.
 */
public class KillerMoves {

    int MAX_KILLERS = 2;

    int MAX_PLY = 100;

    private List<Integer>[] killerMovesWhite = new ArrayList[MAX_PLY];
    private List<Integer>[] killerMovesBlack = new ArrayList[MAX_PLY];

    public void addKiller(Color color, MoveCursor moveCursor, int ply) {
        if (ply < MAX_PLY) {
            int move = moveCursor.getMoveInt();
            List<Integer> kmovesList = getOrCreateKillerList(color, ply);
            if (!kmovesList.contains(move)) {
                kmovesList.add(0, move);
                if (kmovesList.size() > MAX_KILLERS) {
                    kmovesList.remove(MAX_KILLERS - 1);
                }
            }
        }
    }

    public boolean isKiller(Color color, int moveInt, int ply) {
        if (ply > MAX_PLY) {
            return false;
        }
        List<Integer> kmovesList = getOrCreateKillerList(color, ply);
        for (int i = 0; i < kmovesList.size(); i++) {
            if (kmovesList.get(i) == moveInt) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> getOrCreateKillerList(Color color, int ply) {
        List<Integer>[] killerMoves = color == Color.WHITE ? killerMovesWhite : killerMovesBlack;
        List<Integer> kmovesList = killerMoves[ply];
        if (kmovesList == null) {
            kmovesList = new ArrayList<>();
            killerMoves[ply] = kmovesList;
        }
        return kmovesList;
    }
}
