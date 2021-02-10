package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.evaluation.BoardStats;

/**
 * Analyzes pawn structure. Idea copied from TSCP-rust: https://github.com/kristopherjohnson/tscp-rust/blob/master/src/eval.rs
 */
public class PawnStructureEval {

    static class PawnLineChecker {

        // use "impossible" empty values for both sied which are outside the promotion line of each side:
        public static final int W_EMPTY = -1;
        public static final int B_EMPTY = 8;
        // offset because of borders in rank array:
        public static final int RANKOFFSET = 1;

        // pawn ranks with offset 1 to workaround 0th and 7th side ranks (0 is always empt, 9 is always empty as borders)
        int[] rowRank = new int[10];

        // pawns of this color
        int[] pawns;
        // color
        Color color;
        // empty rank marker for this color: the row of the pawns that could never be available (first or last)
        int empty = 0;

        public PawnLineChecker(PieceList.Array pawns, Color color) {
            this.color = color;
            this.pawns = pawns.getArr();
            empty = color == Color.WHITE ? W_EMPTY : B_EMPTY;

            for (int i = 0; i < 10; i++) {
                rowRank[i] = empty;
            }
            for (int i : pawns.getArr()) {
                int x = i % 8 + RANKOFFSET;
                int y = 7 - (i / 8);
                if (color == Color.WHITE && (y < rowRank[x] || rowRank[x] == empty)) {
                    rowRank[x] = y;
                } else if (color == Color.BLACK && (y > rowRank[x] || rowRank[x] == empty)) {
                    rowRank[x] = y;
                }
            }
        }

    }

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {

        PawnLineChecker wChecker = new PawnLineChecker(currBoard.getWhitePieces().getPawns(), Color.WHITE);
        PawnLineChecker bChecker = new PawnLineChecker(currBoard.getBlackPieces().getPawns(), Color.BLACK);

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        return (analyzeWhite(wChecker, bChecker) - analyzeBlack(wChecker, bChecker)) * who2mov;
    }

    public int analyzeWhite(PawnLineChecker wline, PawnLineChecker bline) {
        int r = 0;
        for (int wp : wline.pawns) {
            int x = wp % 8 + PawnLineChecker.RANKOFFSET;
            int y = 7 - (wp / 8);

            // if there's a pawn behind this one, it's doubled
            if (wline.rowRank[x] < y) {
                r += DOUBLED_PAWN_PENALTY;
            }

            // if there aren't any friendly pawns on either side of this one, it's
            // isolated
            if (wline.rowRank[x - 1] == wline.empty && wline.rowRank[x + 1] == wline.empty) {
                r += ISOLATED_PAWN_PENALTY;
            }
            // if it's not isolated, it might be backwards                   (todo check condition for white°)
            else if (wline.rowRank[x - 1] < y && wline.rowRank[x + 1] < y) {
                r += BACKWARDS_PAWN_PENALTY;
            }

            // add a bonus if the pawn is passed (check conditions for white!!)
            if (bline.rowRank[x - 1] >= y
                    && bline.rowRank[x] >= y
                    && bline.rowRank[x + 1] >= y) {
                r += (7 - y) * PASSED_PAWN_BONUS;
            }
        }
        return r;
    }

    public int analyzeBlack(PawnLineChecker wline, PawnLineChecker bline) {
        int r = 0;
        for (int bp : bline.pawns) {
            int x = bp % 8 + PawnLineChecker.RANKOFFSET;
            int y = 7 - (bp / 8);

            // if there's a pawn behind this one, it's doubled
            if (bline.rowRank[x] > y) {
                r += DOUBLED_PAWN_PENALTY;
            }

            // if there aren't any friendly pawns on either side of this one, it's
            // isolated
            if (bline.rowRank[x - 1] == bline.empty && bline.rowRank[x + 1] == bline.empty) {
                r += ISOLATED_PAWN_PENALTY;
            }
            // if it's not isolated, it might be backwards                   (todo check condition for white°)
            else if (bline.rowRank[x - 1] > y && bline.rowRank[x + 1] > y) {
                r += BACKWARDS_PAWN_PENALTY;
            }

            // add a bonus if the pawn is passed (check conditions for white!!)
            if (wline.rowRank[x - 1] <= y
                    && wline.rowRank[x] <= y
                    && wline.rowRank[x + 1] <= y) {

                r += y * PASSED_PAWN_BONUS;
            }
        }
        return r;
    }

}
