package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.*;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.board.bitboard.BB.CenterFiles;
import static org.mattlang.jc.board.bitboard.BB.soutOne;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;

public class ParameterizedSpaceEvaluation implements EvalComponent {

    public static final long RANK_234 = BB.rank2 | BB.rank3 | BB.rank4;
    public static final long WHITE_CENTERFILES = CenterFiles & RANK_234;
    public static final long RANK_567 = BB.rank7 | BB.rank6 | BB.rank5;
    public static final long BLACK_CENTERFILES = CenterFiles & RANK_567;

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

        long SpaceMask = us == WHITE ? WHITE_CENTERFILES : BLACK_CENTERFILES;

        // Find the available squares for our pieces inside the area defined by SpaceMask
        long safe = SpaceMask
                & ~bb.getPawns(us)
                & ~result.getAttacks(Them, FT_PAWN);

        long behind = calcBehind(us, bb);

        int blockedCount = calcBlockedCount(us, bb);

        // Compute space score based on the number of safe squares and number of our pieces
        // increased with number of total blocked pawns in position.
        int bonus = bitCount(safe) + bitCount(behind & safe & ~result.getAttacks(Them, FT_ALL));
        int weight = bitCount(bb.getColorMask(us)) - 3 + Math.min(blockedCount, 9);
        int score = bonus * weight * weight / 16;

        return score;
    }

    private static long calcBehind(Color us, BitChessBoard bb) {
        // Find all squares which are at most three squares behind some friendly pawn
        long behind = bb.getPawns(us);
        switch (us) {
        case WHITE:
            behind |= soutOne(behind);
            behind |= soutOne(soutOne(behind));
            break;
        case BLACK:
            behind |= BB.nortOne(behind);
            behind |= BB.nortOne(BB.nortOne(behind));
            break;
        }
        return behind;
    }

    private static int calcBlockedCount(Color us, BitChessBoard bb) {
        // todo: refactor: blocked (white/black) Pawns are already calculated during pawn evaluation
        long blockedPawns = calcBlockedPawns(us, bb);
        int blocked_count = bitCount(blockedPawns);
        return blocked_count;
    }

    private static long calcBlockedPawns(Color us, BitChessBoard bb) {
        long blockedPawns = 0L;
        switch (us) {
        case WHITE:
            blockedPawns = soutOne(bb.getPawns(nBlack)) & bb.getPawns(nWhite);
            break;
        case BLACK:
            blockedPawns = BB.nortOne(bb.getPawns(nWhite)) & bb.getPawns(nBlack);
            break;
        }
        return blockedPawns;
    }

}
