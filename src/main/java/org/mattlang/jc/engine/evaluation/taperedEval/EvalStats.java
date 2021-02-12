package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.PieceList;

/**
 * Contains eval stats used in some evaluation classes.
 */
public class EvalStats {

    public final int whiteMat;
    public final int blackMat;

    public final PawnRanks wRank;
    public final PawnRanks bRank;

    public EvalStats(BoardRepresentation currBoard) {

        PieceList wp = currBoard.getWhitePieces();
        PieceList bp = currBoard.getBlackPieces();

        int wKingCount = wp.getKing() < 0 ? 0 : 1;
        int bKingCount = bp.getKing() < 0 ? 0 : 1;

        whiteMat = KING_WEIGHT * wKingCount +
                QUEEN_WEIGHT * wp.getQueens().size() +
                ROOK_WEIGHT * wp.getRooks().size() +
                BISHOP_WEIGHT * wp.getBishops().size() +
                KNIGHT_WEIGHT * wp.getKnights().size() +
                PAWN_WEIGHT * wp.getPawns().size();

        blackMat = KING_WEIGHT * bKingCount +
                QUEEN_WEIGHT * bp.getQueens().size() +
                ROOK_WEIGHT * bp.getRooks().size() +
                BISHOP_WEIGHT * bp.getBishops().size() +
                KNIGHT_WEIGHT * bp.getKnights().size() +
                PAWN_WEIGHT * bp.getPawns().size();

        wRank = new PawnRanks(currBoard.getWhitePieces().getPawns(), WHITE);
        bRank = new PawnRanks(currBoard.getBlackPieces().getPawns(), BLACK);
    }
}
