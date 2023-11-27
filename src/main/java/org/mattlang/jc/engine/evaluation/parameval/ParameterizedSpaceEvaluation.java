package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.board.bitboard.BB.CenterFiles;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;

public class ParameterizedSpaceEvaluation implements EvalComponent {

    public static final long RANK_234 = BB.rank2 | BB.rank3 | BB.rank4;
    public static final long RANK_567 = BB.rank7 | BB.rank6 | BB.rank5;

    public static final long FILE_CDEF = BB.C | BB.D | BB.E | BB.F;

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        result.getMgEgScore().addMg(space(result, bitBoard, WHITE) - space(result, bitBoard, BLACK));
    }

    /**
     * computes a space evaluation for a given side, aiming to improve game
     * play in the opening. It is based on the number of safe squares on the four central files
     * on ranks 2 to 4. Completely safe squares behind a friendly pawn are counted twice.
     * Finally, the space bonus is multiplied by a weight which decreases according to occupancy.
     */
    private int space(EvalResult result, BoardRepresentation bitBoard, Color us) {
        BitChessBoard bb = bitBoard.getBoard();

        // Early exit if, for example, both queens or 6 minor pieces have been exchanged
        //        if (pos.non_pawn_material() < SpaceThreshold)
        //            return SCORE_ZERO;

        Color Them = us.invert();

        long SpaceMask =
                us == WHITE ? CenterFiles & (BB.rank2 | BB.rank3 | BB.rank4)
                        : CenterFiles & (BB.rank7 | BB.rank6 | BB.rank5);

        // Find the available squares for our pieces inside the area defined by SpaceMask
        long safe = SpaceMask
                & ~bb.getPawns(us)
                & ~result.getAttacks(Them, FT_PAWN);

        // Find all squares which are at most three squares behind some friendly pawn
        long behind = bb.getPawns(us);
        switch (us) {
        case WHITE:
            behind |= BB.soutOne(behind);
            behind |= BB.soutOne(BB.soutOne(behind));
            break;
        case BLACK:
            behind |= BB.nortOne(behind);
            behind |= BB.nortOne(BB.nortOne(behind));
            break;
        }

        // todo: refactor: blocked (white/black) Pawns are already calculated during pawn evaluation
        long blockedPawns = 0L;
        switch (us) {
        case WHITE:
            blockedPawns = BB.soutOne(bb.getPawns(nBlack)) & bb.getPawns(nWhite);
            break;
        case BLACK:
            blockedPawns = BB.nortOne(bb.getPawns(nWhite)) & bb.getPawns(nBlack);
            break;
        }
        int blocked_count = bitCount(blockedPawns);

        // Compute space score based on the number of safe squares and number of our pieces
        // increased with number of total blocked pawns in position.
        int bonus = bitCount(safe) + bitCount(behind & safe & ~result.getAttacks(Them, FT_ALL));
        int weight = bitCount(bb.getColorMask(us)) - 3 + Math.min(blocked_count, 9);
        int score = bonus * weight * weight / 16;

        return score;
    }




/*
    template<Tracing T> template<Color Us>
    Score Evaluation<T>::space() const {

        // Early exit if, for example, both queens or 6 minor pieces have been exchanged
        if (pos.non_pawn_material() < SpaceThreshold)
            return SCORE_ZERO;

        constexpr Color Them     = ~Us;
        constexpr Direction Down = -pawn_push(Us);
        constexpr Bitboard SpaceMask =
                Us == WHITE ? CenterFiles & (Rank2BB | Rank3BB | Rank4BB)
                        : CenterFiles & (Rank7BB | Rank6BB | Rank5BB);

        // Find the available squares for our pieces inside the area defined by SpaceMask
        Bitboard safe =   SpaceMask
                & ~pos.pieces(Us, PAWN)
                & ~attackedBy[Them][PAWN];

        // Find all squares which are at most three squares behind some friendly pawn
        Bitboard behind = pos.pieces(Us, PAWN);
        behind |= shift<Down>(behind);
        behind |= shift<Down+Down>(behind);

        // Compute space score based on the number of safe squares and number of our pieces
        // increased with number of total blocked pawns in position.
        int bonus = popcount(safe) + popcount(behind & safe & ~attackedBy[Them][ALL_PIECES]);
        int weight = pos.count<ALL_PIECES>(Us) - 3 + std::min(pe->blocked_count(), 9);
        Score score = make_score(bonus * weight * weight / 16, 0);

        if constexpr (T)
        Trace::add(SPACE, Us, score);

        return score;
    }


 */

    //    public int calcNonPawnMat( BoardRepresentation bitBoard) {
    //
    //        BitChessBoard bb = bitBoard.getBoard();
    //
    //
    //        int knightsDiff = bb.getKnightsCount(nWhite) + bb.getKnightsCount(nBlack);
    //        int bishopsDiff = bb.getBishopsCount(nWhite) + bb.getBishopsCount(nBlack);
    //        int rooksDiff = bb.getRooksCount(nWhite) + bb.getRooksCount(nBlack);
    //        int queensDiff = bb.getQueensCount(nWhite) + bb.getQueensCount(nBlack);
    //
    //        result.midGame += pawnMG * pawnsDiff +
    //                knightMG * knightsDiff +
    //                bishopMG * bishopsDiff +
    //                rookMG * rooksDiff +
    //                queenMG * queensDiff;
    //
    //        result.endGame += pawnEG * pawnsDiff +
    //                knightEG * knightsDiff +
    //                bishopEG * bishopsDiff +
    //                rookEG * rooksDiff +
    //                queenEG * queensDiff;
    //    }

    public static final int[] SPACE = { 0, 0, 124, 0, 0, -6, -6, -8, -7, -4, -4, -2, 0, -1, 0, 3, 7 };

    private static int IX_SPACE = 12;

    public static int calculateSpace(EvalResult result, BoardRepresentation bitBoard) {

        BitChessBoard bb = bitBoard.getBoard();
        final long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        final long blackPawns = bb.getPawns(BitChessBoard.nBlack);

        if (whitePawns == 0 && blackPawns == 0) {
            return 0;
        }

        int score = 0;

        score += IX_SPACE
                * Long.bitCount((whitePawns >>> 8) & (bb.getKnights(nWhite) | bb.getBishops(nWhite)) & RANK_234);
        score -= IX_SPACE
                * Long.bitCount((blackPawns << 8) & (bb.getKnights(nBlack) | bb.getBishops(nBlack)) & RANK_567);

        // idea taken from Laser
        long space = whitePawns >>> 8;
        space |= space >>> 8 | space >>> 16;
        score += SPACE[Long.bitCount(bb.getColorMask(nWhite))]
                * Long.bitCount(space & ~whitePawns & ~result.getAttacks(BLACK, FT_PAWN) & FILE_CDEF);
        space = blackPawns << 8;
        space |= space << 8 | space << 16;
        score -= SPACE[Long.bitCount(bb.getColorMask(nBlack))]
                * Long.bitCount(space & ~blackPawns & ~result.getAttacks(WHITE, FT_PAWN) & FILE_CDEF);

        return score;
    }
}
