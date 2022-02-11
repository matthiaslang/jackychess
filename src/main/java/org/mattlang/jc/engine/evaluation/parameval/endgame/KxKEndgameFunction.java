package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.Tools.*;

import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS;

/**
 * Endgame Function which handles KX vs K. This function is used to evaluate positions with
 * king and plenty of material vs a lone king. It simply gives the
 * attacking side a bonus for driving the defending king towards the edge
 * of the board, and for keeping the distance between the two kings small.
 *
 * Should not be called when in check.
 */
public class KxKEndgameFunction implements EndgameFunction{

    private static final int VALUE_KNOWN_WIN = 10000;

    private static final long DarkSquares = 0xAA55AA55AA55AA55L;

    // Used to drive the king towards the edge of the board
    // in KX vs K and KQ vs KR endgames.
    // Values range from 27 (center squares) to 90 (in the corners)
    int push_to_edge(int s) {
        int rd = edgeRankDistance(rankOf(s)), fd = edgeFileDistance(fileOf(s));
        return 90 - (7 * fd * fd / 2 + 7 * rd * rd / 2);
    }

    // Used to drive the king towards A1H8 corners in KBN vs K endgames.
    // Values range from 0 on A8H1 diagonal to 7 in A1H8 corners
    int push_to_corner(int s) {
        return Math.abs(7 - rankOf(s) - fileOf(s));
    }

    // Drive a piece close to or away from another piece
    int push_close(int s1, int s2) {
        return 140 - 20 * distance(s1, s2);
    }

    int push_away(int s1, int s2) {
        return 120 - push_close(s1, s2);
    }

    public int evaluate(BitBoard bitBoard, int stronger, int weaker) {

        BitChessBoard bb = bitBoard.getBoard();

        // Stalemate detection with lone king
        //        if (pos.side_to_move() == weakSide && !MoveList<LEGAL>(pos).size())
        //            return VALUE_DRAW;

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        int result = push_to_edge(weakKing)
                + push_close(strongKing, weakKing);

        if (bb.getQueensCount(stronger) > 0
                || bb.getRooksCount(stronger) > 0
                || (bb.getBishopsCount(stronger) > 0 && bb.getKnightsCount(stronger) > 0)
                || ((bb.getBishops(stronger) & ~DarkSquares) != 0
                && (bb.getBishops(stronger) & DarkSquares) != 0))
            result = Math.min(result + VALUE_KNOWN_WIN, NegaMaxAlphaBetaPVS.VALUE_TB_WIN_IN_MAX_PLY - 1);

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
        return stronger == bitBoard.getSiteToMove().ordinal() ? result : -result;
    }
}
