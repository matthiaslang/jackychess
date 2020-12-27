package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board2;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.EvaluateFunction;

/**
 * Experimental Material Evaluation.
 * <p>
 * https://www.chessprogramming.org/Simplified_Evaluation_Function
 */
public class MaterialNegaMaxEvalOpt implements EvaluateFunction {

    /* King wheight, aka Matt Wheight. */
    public static final int KING_WHEIGHT = 32000;
    public static final int PATT_WEIGHT = 10000;


    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        Board2 board = (Board2) currBoard;
        PieceList wp = board.getWhitePieces();
        PieceList bp = board.getBlackPieces();

        int wKingCount = wp.getKing() < 0 ? 0 : 1;
        int bKingCount = bp.getKing() < 0 ? 0 : 1;

        int score = KING_WHEIGHT * (wKingCount - bKingCount) * who2mov +
                900 * (wp.getQueens().size() - bp.getQueens().size()) * who2mov +
                500 * (wp.getRooks().size() - bp.getRooks().size()) * who2mov +
                330 * (wp.getBishops().size() - bp.getBishops().size()) * who2mov +
                320 * (wp.getKnights().size() - bp.getKnights().size()) * who2mov +
                100 * (wp.getPawns().size() - bp.getPawns().size()) * who2mov +
                // two bishop bonus:
                50 * (wp.getBishops().size() == 2 ? 1 : 0 - bp.getBishops().size() == 2 ? 1 : 0) * who2mov +
                // penalty for two knights:
                -50 * (wp.getKnights().size() == 2 ? 1 : 0 - bp.getKnights().size() == 2 ? 1 : 0) * who2mov
                +
                // penalty for having no pawns (especially in endgame)
                -500 * (wp.getPawns().size() == 0 ? 1 : 0 - bp.getPawns().size() == 0 ? 1 : 0) * who2mov;


        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);
        score += 10 * (wstats.mobility - bstats.mobility) * who2mov +
                20 * (wstats.captures - bstats.captures) * who2mov;

        int whiteMakesPatt = isPatt(wstats, bstats);
        int blackMakesPatt = isPatt(bstats, wstats);

        if (whiteMakesPatt == 1 || blackMakesPatt == 1) {
            // patt dominates the score and resets all previous score calculation:
            score = -PATT_WEIGHT * (whiteMakesPatt - blackMakesPatt) * who2mov;
        }

        return score;

    }

    private int isPatt(BoardStats me, BoardStats other) {
        // if only king of other side can move, we need to take care and further analyse:
        if (Long.bitCount(other.kingMobilityBitBoard) == other.mobility) {
            // diff our mobility/captures possibility with the enemies king mobility:
            long possibleCaptures = me.mobilityBitBoard | me.hypotheticalPawnCaptures;
            if ((possibleCaptures & other.kingMobilityBitBoard) == other.kingMobilityBitBoard) {
                // big penalty for patt!
                // todo or make additional evaluations based on material if we should do accept the patt?
                return 1;
            }
        }
        return 0;
    }
}
