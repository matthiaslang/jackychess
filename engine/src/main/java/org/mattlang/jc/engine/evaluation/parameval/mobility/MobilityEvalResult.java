package org.mattlang.jc.engine.evaluation.parameval.mobility;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.board.bitboard.BB.*;
import static org.mattlang.jc.engine.evaluation.parameval.KingZoneMasks.getKingZoneMask;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMobilityEvaluation;

import lombok.Getter;

@EvalConfigurable(prefix = "positional")
@EvalValueInterval(min = 0, max=100)
@Getter
public class MobilityEvalResult {

    private static final long WHITE_QUEEN_DEVELOPED_MASK = ALL & ~rank1 & ~rank2;
    private static final long BLACK_QUEEN_DEVELOPED_MASK = ALL & ~rank7 & ~rank8;

    public static final long WHITE_BISHOPS_STARTPOS = C1 | BB.F1;
    public static final long BLACK_BISHOPS_STARTPOS = C8 | F8;
    public static final long WHITE_KNIGHT_STARTPOS = B1 | G1;
    public static final long BLACK_KNIGHT_STARTPOS = B8 | G8;

    public int eval;


    public int kingAttCount;
    /**
     * mg/eg combined king attack weight.
     */
    public int kingAttWeightMgEg;
    public int positionalThemes;
    public int blockages;

    private final int MGEG_ONE = MgEgScore.createMgEgScore(1, 1);
    private final int MGEG_TWO = MgEgScore.createMgEgScore(2, 2);

    // parameters:
    /**
     * combined for mg, eg;
     */
    @EvalConfigParam(name = "rookOpen", mgEgCombined = true)
    private int rookOpenMgEg;


    /**
     * combined value for mg+eg;
     */
    @EvalConfigParam(name = "rookHalf", mgEgCombined = true)
    private int rookHalfMgEg;

    @EvalConfigParam(name = "earlyQueenPenalty")
    private int earlyQueenPenalty;

    private long empty;
    private long occupancy;
    private long noOppPawnAttacs;
    private long oppKingZone;
    private long opponentFigsMask;
    private int oppKingPos;
    private long ownPawns;
    private long oppPawns;


    public MobilityEvalResult(boolean forTuning, EvalConfig config) {
    }

    public void clear() {
        eval = 0;
        kingAttCount = 0;
        kingAttWeightMgEg = 0;
        positionalThemes = 0;
        blockages = 0;
    }

    public void countFigureMobilityVals(MobFigParams params, int figPos, long attacks) {

        long mobility = attacks & empty & noOppPawnAttacs;
        long captures = attacks & opponentFigsMask;
        long kingZoneAttacs = attacks & oppKingZone;
        int tropism = getTropism(figPos, oppKingPos);

        // mobility count is mobility to empty fields as well as captures of enemy pieces:
        int mobCount = bitCount(mobility) + bitCount(captures);
        int currKingAttCount = bitCount(kingZoneAttacs);

        eval += params.mobility.calc(mobCount);

        kingAttCount += currKingAttCount;
        kingAttWeightMgEg += params.kingAtt.calc(currKingAttCount);

        eval += params.tropism.calc(tropism);

    }

    public void rookOpenFiles(int rook) {
        long rookMask = 1L << rook;

        long fileFilled = BB.fileFill(rookMask);
        boolean ownPawnOnFile = (fileFilled & ownPawns) != 0;
        boolean oppPawnOnFile = (fileFilled & oppPawns) != 0;

        // fully open file:
        if (!ownPawnOnFile && !oppPawnOnFile) {
            // todo make a field from rookOpen...
            eval += rookOpenMgEg;
            if (Tools.fileDistance(rook, oppKingPos) < 2) {
                kingAttWeightMgEg += MGEG_ONE;
            }
        } else if (!ownPawnOnFile && oppPawnOnFile) {
            // half open:
            eval += rookHalfMgEg;
            if (Tools.fileDistance(rook, oppKingPos) < 2) {
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


    public void setEarlyQueenPenalty(int earlyQueenPenalty) {
        this.earlyQueenPenalty = earlyQueenPenalty;
    }

    public void init(Color side, BitChessBoard bb) {
        Color xside = side.invert();

        long ownFigsMask = bb.getColorMask(side);
        opponentFigsMask = bb.getColorMask(xside);
        empty = ~ownFigsMask & ~opponentFigsMask;
        occupancy = ownFigsMask | opponentFigsMask;

        long oppKingMask = bb.getPieceSet(FT_KING, xside);
        oppKingPos = Long.numberOfTrailingZeros(oppKingMask);
        oppKingZone = getKingZoneMask(xside.ordinal(), oppKingPos);

        // opponents pawn attacs. we exclude fields under opponents pawn attack from our mobility
        long oppPawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, side);
        noOppPawnAttacs = ~oppPawnAttacs;

        ownPawns = bb.getPieceSet(FT_PAWN, side);
        oppPawns = bb.getPieceSet(FT_PAWN, xside);
    }

    /**
     * Weights the manhattan distance to the king from 0 to 13.
     * 0 == fartest from king. 13 == nearest to king
     *
     * @param sq1
     * @param sq2
     * @return
     */
    private int getTropism(int sq1, int sq2) {
        return 14 - Tools.manhattanDistance(sq1, sq2);
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
