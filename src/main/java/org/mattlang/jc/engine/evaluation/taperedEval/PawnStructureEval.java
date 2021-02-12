package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.Color;

/**
 * Analyzes pawn structure. Idea copied from TSCP-rust: https://github.com/kristopherjohnson/tscp-rust/blob/master/src/eval.rs
 */
public class PawnStructureEval {


    public int eval(EvalStats evalStats, Color who2Move) {

        PawnRanks wChecker = evalStats.wRank;
        PawnRanks bChecker = evalStats.bRank;

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        return (analyzeWhite(wChecker, bChecker) - analyzeBlack(wChecker, bChecker)) * who2mov;
    }

    public int analyzeWhite(PawnRanks wline, PawnRanks bline) {
        int r = 0;
        for (int wp : wline.pawns) {
            int x = wp % 8 + PawnRanks.RANKOFFSET;
            int y = 7 - (wp / 8);

            // if there's a pawn behind this one, it's doubled
            if (wline.rank[x] < y) {
                r += DOUBLED_PAWN_PENALTY;
            }

            // if there aren't any friendly pawns on either side of this one, it's
            // isolated
            if (wline.isRankEmpty(x - 1) && wline.isRankEmpty(x + 1)) {
                r += ISOLATED_PAWN_PENALTY;
            }
            // if it's not isolated, it might be backwards
            else if (wline.rank[x - 1] < y && wline.rank[x + 1] < y) {
                r += BACKWARDS_PAWN_PENALTY;
            }

            // add a bonus if the pawn is passed
            if (bline.rank[x - 1] >= y
                    && bline.rank[x] >= y
                    && bline.rank[x + 1] >= y) {
                r += (7 - y) * PASSED_PAWN_BONUS;
            }
        }
        return r;
    }

    public int analyzeBlack(PawnRanks wline, PawnRanks bline) {
        int r = 0;
        for (int bp : bline.pawns) {
            int x = bp % 8 + PawnRanks.RANKOFFSET;
            int y = 7 - (bp / 8);

            // if there's a pawn behind this one, it's doubled
            if (bline.rank[x] > y) {
                r += DOUBLED_PAWN_PENALTY;
            }

            // if there aren't any friendly pawns on either side of this one, it's
            // isolated
            if (bline.isRankEmpty(x - 1) && bline.isRankEmpty(x + 1)) {
                r += ISOLATED_PAWN_PENALTY;
            }
            // if it's not isolated, it might be backwards
            else if (bline.rank[x - 1] > y && bline.rank[x + 1] > y) {
                r += BACKWARDS_PAWN_PENALTY;
            }

            // add a bonus if the pawn is passed
            if (wline.rank[x - 1] <= y
                    && wline.rank[x] <= y
                    && wline.rank[x + 1] <= y) {

                r += y * PASSED_PAWN_BONUS;
            }
        }
        return r;
    }

}
