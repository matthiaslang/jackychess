package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.engine.evaluation.Weights.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;

public class Evaluations {

    public static final int materialEval(BoardRepresentation currBoard, Color who2Move) {

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

        return score;
    }

}
