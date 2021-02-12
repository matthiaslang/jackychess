package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.engine.evaluation.Weights.*;
import static org.mattlang.jc.engine.evaluation.taperedEval.PawnRanks.RANKOFFSET;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.Weights;

/**
 * Analyzes King safety. Inspired from tscp rust implementation: https://github.com/kristopherjohnson/tscp-rust/blob/master/src/eval.rs
 */
public class KingSafetyEval {

    public int eval(BoardRepresentation currBoard, EvalStats evalStats, Color who2Move) {
        int who2mov = who2Move == WHITE ? 1 : -1;
        return (analyzeWhite(currBoard, evalStats) - analyzeBlack(currBoard, evalStats)) * who2mov;
    }

    private int analyzeBlack(BoardRepresentation board, EvalStats evalStats) {
        int kingPos = board.getBlackPieces().getKing();
        int x = kingPos % 8 + RANKOFFSET;

        PawnRanks wRank = evalStats.wRank;
        PawnRanks bRank = evalStats.bRank;

        int r = 0;
        // if the king is castled, use a special function to evaluate the pawns on
        // the appropriate side
        if (x < 3) {
            r += eval_dkp(wRank, bRank, 1);
            r += eval_dkp(wRank, bRank, 2);
            r += eval_dkp(wRank, bRank, 3) / 2; // problems with pawns on the c & f files are not as severe
        } else if (x > 4) {
            r += eval_dkp(wRank, bRank, 8);
            r += eval_dkp(wRank, bRank, 7);
            r += eval_dkp(wRank, bRank, 6) / 2;
        }
        // otherwise just assess a penalty if there are open files near the king
        else {
            for (int i = x - 1; i < x + 1; i++) {
                if (wRank.isRankEmpty(i) && bRank.isRankEmpty(i)) {
                    r += OPEN_FILE_NEAR_KING_PENALTY;
                }
            }
        }

        // scale the king safely value according to the opponent's material; the
        // premise is that your king safety can only be bad if the opponent has
        // enough pieces to attack you.
        r *= (evalStats.whiteMat - KING_WEIGHT);
        r /= (Weights.MAX_REAL_MAT);

        return r;
    }

    private int analyzeWhite(BoardRepresentation board, EvalStats evalStats) {
        int kingPos = board.getWhitePieces().getKing();
        int x = kingPos % 8 + RANKOFFSET;

        PawnRanks wRank = evalStats.wRank;
        PawnRanks bRank = evalStats.bRank;

        int r = 0;
        // if the king is castled, use a special function to evaluate the pawns on
        // the appropriate side
        if (x < 3) {
            r += eval_lkp(wRank, bRank, 1);
            r += eval_lkp(wRank, bRank, 2);
            r += eval_lkp(wRank, bRank, 3) / 2; // problems with pawns on the c & f files are not as severe
        } else if (x > 4) {
            r += eval_lkp(wRank, bRank, 8);
            r += eval_lkp(wRank, bRank, 7);
            r += eval_lkp(wRank, bRank, 6) / 2;
        }
        // otherwise just assess a penalty if there are open files near the king
        else {
            for (int i = x - 1; i < x + 1; i++) {
                if (wRank.isRankEmpty(i) && bRank.isRankEmpty(i)) {
                    r += OPEN_FILE_NEAR_KING_PENALTY;
                }
            }
        }

        // scale the king safely value according to the opponent's material; the
        // premise is that your king safety can only be bad if the opponent has
        // enough pieces to attack you.
        r *= (evalStats.blackMat - KING_WEIGHT);
        r /= (Weights.MAX_REAL_MAT);

        return r;
    }

    private int eval_lkp(PawnRanks wRank, PawnRanks bRank, int f) {
        int r = 0;

        if (wRank.isRankEmpty(f)) {
            r += KING_SAFETY_NO_PAWN_ON_THIS_FILE;// no pawn on this file
        } else {
            switch (wRank.rank[f]) {
            case 6:
                break;      // pawn hasn't moved
            case 5:
                r += KING_SAFETY_PAWN_MOVED_ONE_SQUARE;
                break; // pawn moved one square
            default:
                r += KING_SAFETY_PAWN_MOVED_MORE_THAN_A_SQUARE; // pawn moved more than one square
            }
        }

        if (bRank.isRankEmpty(f)) {
            r -= 15; // no enemy pawn
        } else {
            switch (bRank.rank[f]) {
            case 5:
                r += KING_SAFETY_ENEMY_ON_THIRD_RANK;
                break; // enemy pawn on the 3rd rank
            case 4:
                r += KING_SAFETY_ENEMY_ON_FOURTH_RANK;  // enemy pawn on the 4th rank
            default:
                // otherwise do not weight
            }
        }

        return r;
    }

    private int eval_dkp(PawnRanks wRank, PawnRanks bRank, int f) {
        int r = 0;

        if (bRank.isRankEmpty(f)) {
            r += KING_SAFETY_NO_PAWN_ON_THIS_FILE;// no pawn on this file
        } else {
            switch (wRank.rank[f]) {
            case 1:
                break;      // pawn hasn't moved
            case 2:
                r += KING_SAFETY_PAWN_MOVED_ONE_SQUARE;
                break; // pawn moved one square
            default:
                r += KING_SAFETY_PAWN_MOVED_MORE_THAN_A_SQUARE; // pawn moved more than one square
            }
        }

        if (wRank.isRankEmpty(f)) {
            r -= 15; // no enemy pawn
        } else {
            switch (bRank.rank[f]) {
            case 2:
                r += KING_SAFETY_ENEMY_ON_THIRD_RANK;
                break; // enemy pawn on the 3rd rank
            case 3:
                r += KING_SAFETY_ENEMY_ON_FOURTH_RANK;  // enemy pawn on the 4th rank
            default:
                // otherwise do not weight
            }
        }

        return r;
    }
}
