package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.Weights;

/**
 * Analyzes that we have more material in endgame to have a chance to win.
 */
public class EndGameMaterialEval {

    /**
     * A simple rule says, that we need at least ~4 pawns more material to win in endgame. otherwise it could be impossible.
     * Of course this is oversimplified and should verified by more concrete material considerations, but for now this is good enough.
     */
    public static final int MINIMUM_OVERHEADMAT = 500;

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int wKingCount = wp.getKing() < 0 ? 0 : 1;
        int bKingCount = bp.getKing() < 0 ? 0 : 1;

        int whiteMat = KING_WEIGHT * wKingCount +
                QUEEN_WEIGHT * wp.getQueens().size() +
                ROOK_WEIGHT * wp.getRooks().size() +
                BISHOP_WEIGHT * wp.getBishops().size() +
                KNIGHT_WEIGHT * wp.getKnights().size() +
                PAWN_WEIGHT * wp.getPawns().size();

        int blackMat = KING_WEIGHT * bKingCount +
                QUEEN_WEIGHT * bp.getQueens().size() +
                ROOK_WEIGHT * bp.getRooks().size() +
                BISHOP_WEIGHT * bp.getBishops().size() +
                KNIGHT_WEIGHT * bp.getKnights().size() +
                PAWN_WEIGHT * bp.getPawns().size();


        int wTooLessMat = whiteMat < MINIMUM_OVERHEADMAT + KING_WEIGHT ? Weights.TOO_LESS_MATERIAL_PENALTY : 0;
        int bTooLessMat = blackMat < MINIMUM_OVERHEADMAT + KING_WEIGHT ? Weights.TOO_LESS_MATERIAL_PENALTY : 0;

        // we need more than the minimum overhead to have at least a chance to win:
        int score = (wTooLessMat - bTooLessMat) * who2mov;

        // and we should keep that minimum more than the opponent to have a real chance:
        int wNotEnoughMoreMat = whiteMat-blackMat < MINIMUM_OVERHEADMAT  ? NOT_ENOUGH_MORE_MATERIAL_THAN_OPPONENT : 0;
        int bNotEnoughMoreMat = blackMat-whiteMat < MINIMUM_OVERHEADMAT ? NOT_ENOUGH_MORE_MATERIAL_THAN_OPPONENT : 0;

        score+= (wNotEnoughMoreMat - bNotEnoughMoreMat) *who2mov;

        return score;
    }

}
