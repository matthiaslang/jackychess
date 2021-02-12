package org.mattlang.jc.engine.evaluation.taperedEval;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;

/**
 * For analysing the pawn ranks of one side.
 * Ranks are inverse to chess board rows: row 8 is rank 0, row 7 is rank 1, etc.
 * 
 */
public class PawnRanks {

    // use "impossible" empty values for both sides which are outside the promotion line of each side:
    public static final int W_EMPTY = -1;
    public static final int B_EMPTY = 8;
    // offset because of borders in rank array:
    public static final int RANKOFFSET = 1;

    // pawn ranks with offset 1 to workaround 0th and 7th side ranks (0 is always empt, 9 is always empty as borders)
    int[] rank = new int[10];

    // pawns of this color
    int[] pawns;
    // color
    Color color;
    // empty rank marker for this color: the row of the pawns that could never be available (first or last)
    int empty = 0;

    public PawnRanks(PieceList.Array pawns, Color color) {
        this.color = color;
        this.pawns = pawns.getArr();
        empty = color == Color.WHITE ? W_EMPTY : B_EMPTY;

        for (int i = 0; i < 10; i++) {
            rank[i] = empty;
        }
        for (int i : pawns.getArr()) {
            int x = i % 8 + RANKOFFSET;
            int y = 7 - (i / 8);
            if (color == Color.WHITE && (y < rank[x] || rank[x] == empty)) {
                rank[x] = y;
            } else if (color == Color.BLACK && (y > rank[x] || rank[x] == empty)) {
                rank[x] = y;
            }
        }
    }

    public final boolean isRankEmpty(int x) {
        return rank[x] == empty;
    }
}
