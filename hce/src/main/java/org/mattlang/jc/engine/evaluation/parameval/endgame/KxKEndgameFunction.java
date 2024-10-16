package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.Tools.*;
import static org.mattlang.jc.engine.evaluation.Weights.VALUE_TB_WIN_IN_MAX_PLY;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

/**
 * Endgame Function which handles KX vs K. This function is used to evaluate positions with
 * king and plenty of material vs a lone king. It simply gives the
 * attacking side a bonus for driving the defending king towards the edge
 * of the board, and for keeping the distance between the two kings small.
 *
 * Should not be called when in check.
 */
public class KxKEndgameFunction implements EndgameFunction {

    private static final int VALUE_KNOWN_WIN = 10000;

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        EndgameFunction.assertMat(board, weaker, 0);

        // Stalemate detection with lone king
        //        if (pos.side_to_move() == weakSide && !MoveList<LEGAL>(pos).size())
        //            return VALUE_DRAW;

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = matEvaluation.evalEndGameMaterialOfSide(board, stronger)
                + push_to_edge(weakKing)
                + push_close(strongKing, weakKing);

        if (bb.getQueensCount(stronger) > 0
                || bb.getRooksCount(stronger) > 0
                || (bb.getBishopsCount(stronger) > 0 && bb.getKnightsCount(stronger) > 0)
                || ((bb.getBishops(stronger) & ~DarkSquares) != 0
                && (bb.getBishops(stronger) & DarkSquares) != 0))
            result = Math.min(result + VALUE_KNOWN_WIN, VALUE_TB_WIN_IN_MAX_PLY - 1);

        //
        //        int result =  pos.non_pawn_material(strongSide)
        //                + pos.count<PAWN>(strongSide) * PawnValueEg
        //                + push_to_edge(weakKing)
        //                + push_close(strongKing, weakKing);
        //
        //        if (   pos.count<QUEEN>(strongSide)
        //                || pos.count<ROOK>(strongSide)
        //                ||(pos.count<BISHOP>(strongSide) && pos.count<KNIGHT>(strongSide))
        //                || (   (pos.pieces(strongSide, BISHOP) & ~DarkSquares)
        //                && (pos.pieces(strongSide, BISHOP) &  DarkSquares)))
        //            result = std::min(result + VALUE_KNOWN_WIN, VALUE_TB_WIN_IN_MAX_PLY - 1);
        //
        return stronger == board.getSiteToMove() ? result : -result;
    }
}
