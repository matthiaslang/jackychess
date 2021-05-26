package org.mattlang.jc.engine.search;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

public class HistoryHeuristic {

    // history[side2move][move.from][move.to] += depth*depth; // 1 << depth
    private int[][][] posHistory = new int[2][64][64];

    private int[][][] posFigHistory = new int[2][6][64];

    public void update(Color color, Move move, int depth) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        int fig = move.getFigureType();
        posHistory[colorIdx][move.getFromIndex()][move.getToIndex()] += depth * depth;

        posFigHistory[colorIdx][fig][move.getToIndex()] += depth * depth;
    }

    public int calcValue(Move move, Color color) {
        int colorIdx = color == Color.WHITE ? 0 : 1;
        int fig = move.getFigureType();

        return posFigHistory[colorIdx][fig][move.getToIndex()] +  posFigHistory[colorIdx][fig][move.getToIndex()];
    }
}
