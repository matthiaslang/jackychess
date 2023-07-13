package org.mattlang.jc.engine.evaluation.parameval.mobility;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.bitboard.BB.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;

import lombok.Getter;

@Getter
public class MobilityEvalResult {

    private static final long WHITE_QUEEN_DEVELOPED_MASK = ALL & ~rank1 & ~rank2;
    private static final long BLACK_QUEEN_DEVELOPED_MASK = ALL & ~rank7 & ~rank8;

    public static final long WHITE_BISHOPS_STARTPOS = C1 | BB.F1;
    public static final long BLACK_BISHOPS_STARTPOS = C8 | F8;
    public static final long WHITE_KNIGHT_STARTPOS = B1 | G1;
    public static final long BLACK_KNIGHT_STARTPOS = B8 | G8;
    public static final String ROOK_OPEN = "rookOpen";
    public static final String ROOK_HALF = "rookHalf";
    public static final String EARLY_QUEEN_PENALTY = "earlyQueenPenalty";
    //    public static final String KING_BLOCKS_ROOK_PENALTY = "kingBlocksRookPenalty";
    //    public static final String BLOCK_CENTRAL_PAWN_PENALTY = "blockCentralPawnPenalty";
    //    public static final String BISHOP_TRAPPED_A_7_PENALTY = "bishopTrappedA7Penalty";
    //    public static final String BISHOP_TRAPPED_A_6_PENALTY = "bishopTrappedA6Penalty";
    //    public static final String KNIGHT_TRAPPED_A_8_PENALTY = "knightTrappedA8Penalty";
    //    public static final String KNIGHT_TRAPPED_A_7_PENALTY = "knightTrappedA7Penalty";
    //    public static final String C_3_KNIGHT_PENALTY = "c3KnightPenalty";
    //    public static final String RETURNING_BISHOP = "returningBishop";

    public int mobilityMGEG;

    public int captures;
    public int kingAttCount;
    /**
     * mg/eg combined king attack weight.
     */
    public int kingAttWeightMgEg;
    public int tropismMGEG;
    public int connectivity;
    public int positionalThemes;
    public int blockages;

    private final int MGEG_ONE = MgEgScore.createMgEgScore(1, 1);
    private final int MGEG_TWO = MgEgScore.createMgEgScore(2, 2);

    // parameters:

    private int rookOpen;

    /**
     * combined for mg, eg; currently we use only one value at all..
     */
    private int rookOpenMgEg;

    private int rookHalf;
    /**
     * combined value for mg+eg; currently we use only one value at all..
     */
    private int rookHalfMgEg;
    private int earlyQueenPenalty;

    //    private int kingBlocksRookPenalty;
    //    private int blockCentralPawnPenalty;
    //    private int bishopTrappedA7Penalty;
    //    private int bishopTrappedA6Penalty;
    //    private int knightTrappedA8Penalty;
    //    private int knightTrappedA7Penalty;
    //    private int c3KnightPenalty;
    //    private int returningBishop;

    //    private boolean blockEvalDeactivated = false;

    public MobilityEvalResult(boolean forTuning, EvalConfig config) {

        rookOpen = config.getPosIntProp(ROOK_OPEN);
        rookOpenMgEg = MgEgScore.createMgEgScore(rookOpen, rookOpen);

        rookHalf = config.getPosIntProp(ROOK_HALF);
        rookHalfMgEg = MgEgScore.createMgEgScore(rookHalf, rookHalf);

        earlyQueenPenalty = config.getPosIntProp(EARLY_QUEEN_PENALTY);

        /* trapped and blocked pieces */
        //        kingBlocksRookPenalty = config.getPosIntProp(KING_BLOCKS_ROOK_PENALTY);
        //        blockCentralPawnPenalty = config.getPosIntProp(BLOCK_CENTRAL_PAWN_PENALTY);
        //        bishopTrappedA7Penalty = config.getPosIntProp(BISHOP_TRAPPED_A_7_PENALTY);
        //        bishopTrappedA6Penalty = config.getPosIntProp(BISHOP_TRAPPED_A_6_PENALTY);
        //        knightTrappedA8Penalty = config.getPosIntProp(KNIGHT_TRAPPED_A_8_PENALTY);
        //        knightTrappedA7Penalty = config.getPosIntProp(KNIGHT_TRAPPED_A_7_PENALTY);
        //
        //        c3KnightPenalty = config.getPosIntProp(C_3_KNIGHT_PENALTY);
        //
        //        returningBishop = config.getPosIntProp(RETURNING_BISHOP);
        //
        //        blockEvalDeactivated = !forTuning &&
        //                kingBlocksRookPenalty + blockCentralPawnPenalty + bishopTrappedA7Penalty + bishopTrappedA6Penalty
        //                        + knightTrappedA8Penalty + knightTrappedA7Penalty
        //                        + c3KnightPenalty + returningBishop == 0;
    }

    public void clear() {
        mobilityMGEG = 0;
        captures = 0;
        kingAttCount = 0;
        kingAttWeightMgEg = 0;
        tropismMGEG = 0;
        connectivity = 0;
        positionalThemes = 0;
        blockages = 0;
    }

    public void countFigureVals(MobFigParams params,
            long mobility,
            long captures,
            long kingZoneAttacs,
            int tropism) {

        // mobility count is mobility to empty fields as well as captures of enemy pieces:
        int mobCount = bitCount(mobility) + bitCount(captures);
        int currKingAttCount = bitCount(kingZoneAttacs);

        mobilityMGEG += params.mobilityMGEG.calc(mobCount);

        kingAttCount += currKingAttCount;
        kingAttWeightMgEg += params.kingAttMgEg.calc(currKingAttCount);

        tropismMGEG += params.tropismMGEG.calc(tropism);

    }

    public void rookOpenFiles(int rook, int otherKing, long ownPawns,
            long oppPawns) {
        long rookMask = 1L << rook;

        long fileFilled = BB.fileFill(rookMask);
        boolean ownPawnOnFile = (fileFilled & ownPawns) != 0;
        boolean oppPawnOnFile = (fileFilled & oppPawns) != 0;

        // fully open file:
        if (!ownPawnOnFile && !oppPawnOnFile) {
            // todo make a field from rookOpen...
            mobilityMGEG += rookOpenMgEg;
            if (Tools.colDistance(rook, otherKing) < 2) {
                kingAttWeightMgEg += MGEG_ONE;
            }
        } else if (!ownPawnOnFile && oppPawnOnFile) {
            // half open:
            mobilityMGEG += rookHalfMgEg;
            if (Tools.colDistance(rook, otherKing) < 2) {
                kingAttWeightMgEg += MGEG_TWO;
            }
        }

    }

    public void evalEarlyDevelopedQueen(long queenBB, long bishopBB, long knightBB, Color side) {

        /**************************************************************************
         *  A queen should not be developed too early                              *
         **************************************************************************/

        if (earlyQueenPenalty != 0) {
            if ((side == WHITE && (queenBB & WHITE_QUEEN_DEVELOPED_MASK) != 0)) {

                positionalThemes -=
                        (bitCount(WHITE_BISHOPS_STARTPOS & bishopBB) + bitCount(WHITE_KNIGHT_STARTPOS & knightBB))
                                * earlyQueenPenalty;
            } else if ((side == BLACK && (queenBB & BLACK_QUEEN_DEVELOPED_MASK) != 0)) {

                positionalThemes -=
                        (bitCount(BLACK_BISHOPS_STARTPOS & bishopBB) + bitCount(BLACK_KNIGHT_STARTPOS & knightBB))
                                * earlyQueenPenalty;
            }

        }
    }

    public void setRookOpen(int rookOpen) {
        this.rookOpen = rookOpen;
        rookOpenMgEg = MgEgScore.createMgEgScore(rookOpen, rookOpen);
    }

    public void setRookHalf(int rookHalf) {
        this.rookHalf = rookHalf;
        rookHalfMgEg = MgEgScore.createMgEgScore(rookHalf, rookHalf);
    }

    public void setEarlyQueenPenalty(int earlyQueenPenalty) {
        this.earlyQueenPenalty = earlyQueenPenalty;
    }

    /******************************************************************************
     *                             Pattern detection                               *
     ******************************************************************************/

    //    public void blockedPieces(BitChessBoard bb, Color side) {
    //        if (blockEvalDeactivated) {
    //            return;
    //        }
    //
    //        Color oppo = side.invert();
    //
    //        long ownFigsMask = bb.getColorMask(side);
    //        long opponentFigsMask = bb.getColorMask(oppo);
    //        long occupancy = ownFigsMask | opponentFigsMask;
    //
    //        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
    //        long pawnBB = bb.getPieceSet(FT_PAWN, side);
    //        long pawnOppoBB = bb.getPieceSet(FT_PAWN, oppo);
    //        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
    //        long kingBB = bb.getPieceSet(FT_KING, side);
    //        long rookBB = bb.getPieceSet(FT_ROOK, side);
    //
    //        // Fieldwrapper fw=new fieldMapper(side);
    //        // fm.c1.isSet(bb)
    //
    //        // central pawn blocked, bishop hard to develop
    //        if (Fields.C1.isSet(bishopBB, side)
    //                && Fields.D2.isSet(pawnBB, side)
    //                && Fields.D3.isSet(occupancy, side))
    //            blockages -= blockCentralPawnPenalty;
    //
    //        if (F1.isSet(bishopBB, side)
    //                && E2.isSet(pawnBB, side)
    //                && Fields.E3.isSet(occupancy, side))
    //            blockages -= blockCentralPawnPenalty;
    //
    //        // trapped knight
    //        if (Fields.A8.isSet(knightBB, side)
    //                && (Fields.A7.isSet(pawnOppoBB, side) || Fields.C7.isSet(pawnOppoBB, side)))
    //            blockages -= knightTrappedA8Penalty;
    //
    //        if (Fields.H8.isSet(knightBB, side)
    //                && (Fields.H7.isSet(pawnOppoBB, side) || Fields.F7.isSet(pawnOppoBB, side)))
    //            blockages -= knightTrappedA8Penalty;
    //
    //        if (Fields.A7.isSet(knightBB, side)
    //                && Fields.A6.isSet(pawnOppoBB, side)
    //                && Fields.B7.isSet(pawnOppoBB, side))
    //            blockages -= knightTrappedA7Penalty;
    //
    //        if (Fields.H7.isSet(knightBB, side)
    //                && Fields.H6.isSet(pawnOppoBB, side)
    //                && Fields.G7.isSet(pawnOppoBB, side))
    //            blockages -= knightTrappedA7Penalty;
    //
    //        // knight blocking queenside pawns
    //        if (Fields.C3.isSet(knightBB, side)
    //                && Fields.C2.isSet(pawnBB, side)
    //                && Fields.D2.isSet(pawnBB, side)
    //                && !Fields.E4.isSet(pawnBB, side))
    //            blockages -= c3KnightPenalty;
    //
    //        // trapped bishop
    //        if (Fields.A7.isSet(bishopBB, side)
    //                && Fields.B6.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA7Penalty;
    //
    //        if (Fields.H7.isSet(bishopBB, side)
    //                && Fields.G6.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA7Penalty;
    //
    //        if (Fields.B8.isSet(bishopBB, side)
    //                && Fields.C7.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA7Penalty;
    //
    //        if (Fields.G8.isSet(bishopBB, side)
    //                && Fields.F7.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA7Penalty;
    //
    //        if (Fields.A6.isSet(bishopBB, side)
    //                && Fields.B5.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA6Penalty;
    //
    //        if (Fields.H6.isSet(bishopBB, side)
    //                && Fields.G5.isSet(pawnOppoBB, side))
    //            blockages -= bishopTrappedA6Penalty;
    //
    //        // bishop on initial sqare supporting castled king
    //        if (Fields.F1.isSet(bishopBB, side)
    //                && Fields.G1.isSet(kingBB, side))
    //            blockages += returningBishop;
    //
    //        if (Fields.C1.isSet(bishopBB, side)
    //                && Fields.B1.isSet(kingBB, side))
    //            blockages += returningBishop;
    //
    //        // uncastled king blocking own rook
    //        if ((Fields.F1.isSet(kingBB, side) || Fields.G1.isSet(kingBB, side))
    //                && (Fields.H1.isSet(rookBB, side) || Fields.G1.isSet(rookBB, side)))
    //            blockages -= kingBlocksRookPenalty;
    //
    //        if ((Fields.C1.isSet(kingBB, side) || Fields.B1.isSet(kingBB, side))
    //                && (Fields.A1.isSet(rookBB, side) || Fields.B1.isSet(rookBB, side)))
    //            blockages -= kingBlocksRookPenalty;
    //
    //    }
}
