package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.Factory;
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

    BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int wKingCount = wp.getKing() < 0 ? 0 : 1;
        int bKingCount = bp.getKing() < 0 ? 0 : 1;

        int score = KING_WEIGHT * (wKingCount - bKingCount) * who2mov +
                QUEEN_WEIGHT * (wp.getQueens().size() - bp.getQueens().size()) * who2mov +
                ROOK_WEIGHT * (wp.getRooks().size() - bp.getRooks().size()) * who2mov +
                BISHOP_WEIGHT * (wp.getBishops().size() - bp.getBishops().size()) * who2mov +
                KNIGHT_WEIGHT * (wp.getKnights().size() - bp.getKnights().size()) * who2mov +
                PAWN_WEIGHT * (wp.getPawns().size() - bp.getPawns().size()) * who2mov +
                // two bishop bonus:
                TWO_BISHOP_BONUS * (wp.getBishops().size() == 2 ? 1 : 0 - bp.getBishops().size() == 2 ? 1 : 0) * who2mov
                +
                // penalty for two knights:
                TWO_ROOKS_PENALTY * (wp.getKnights().size() == 2 ? 1 : 0 - bp.getKnights().size() == 2 ? 1 : 0)
                        * who2mov
                +
                // penalty for having no pawns (especially in endgame)
                NO_PAWNS_PENALTY * (wp.getPawns().size() == 0 ? 1 : 0 - bp.getPawns().size() == 0 ? 1 : 0) * who2mov;


        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);
        score += COMMON_MOBILITY_WEIGHT * (wstats.mobility - bstats.mobility) * who2mov +
                COMMON_CAPTURABILITY_WEIGHT * (wstats.captures - bstats.captures) * who2mov;

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
