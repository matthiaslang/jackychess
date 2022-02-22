package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BB.*;
import static org.mattlang.jc.board.bitboard.Fields.E2;
import static org.mattlang.jc.board.bitboard.Fields.F1;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.*;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.functions.KingAttackFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.MobLinFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.TropismFun;

/**
 * Paremeterized Mobility Evaluation.
 */
public class ParameterizedMobilityEvaluation implements EvalComponent {

    private static final long WHITE_QUEEN_DEVELOPED_MASK = ALL & ~rank1 & ~rank2;
    private static final long BLACK_QUEEN_DEVELOPED_MASK = ALL & ~rank7 & ~rank8;

    public static final long WHITE_BISHOPS_STARTPOS = C1 | BB.F1;
    public static final long BLACK_BISHOPS_STARTPOS = C8 | F8;
    public static final long WHITE_KNIGHT_STARTPOS = B1 | G1;
    public static final long BLACK_KNIGHT_STARTPOS = B8 | G8;

    private static int[] SAFETYTABLE = {
            0, 0, 1, 2, 3, 5, 7, 9, 12, 15,
            18, 22, 26, 30, 35, 39, 44, 50, 56, 62,
            68, 75, 82, 85, 89, 97, 105, 113, 122, 131,
            140, 150, 169, 180, 191, 202, 213, 225, 237, 248,
            260, 272, 283, 295, 307, 319, 330, 342, 354, 366,
            377, 389, 401, 412, 424, 436, 448, 459, 471, 483,
            494, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500
    };

    public static final int IWHITE = Color.WHITE.ordinal();
    public static final int IBLACK = Color.BLACK.ordinal();

    public static final int MOBILITY_MG = 0;
    public static final int MOBILITY_EG = 1;
    public static final int CAPTURES = 2;
    public static final int KING_ATT_COUNT = 3;
    public static final int KING_ATT_WEIGHT = 4;
    public static final int TROPISM_MG = 5;
    public static final int TROPISM_EG = 6;

    public static final int CONNECTIVITY = 7;
    public static final int POSITIONAL_THEMES = 8;
    public static final int BLOCKAGES = 9;

    public static final int MAX_TYPE_INDEX = BLOCKAGES + 1;

    private final int rookOpen;
    private final int rookHalf;
    private final int earlyQueenPenalty;

    private final int kingBlocksRookPenalty;
    private final int blockCentralPawnPenalty;
    private final int bishopTrappedA7Penalty;
    private final int bishopTrappedA6Penalty;
    private final int knightTrappedA8Penalty;
    private final int knightTrappedA7Penalty;
    private final int c3KnightPenalty;
    private final int returningBishop;
    /**
     * splitted results by color, and type of evaluation, e.g. mobility, captures, connectivity).
     * They are splitted mainly for debugging purpose.
     */
    public int[][] results = new int[2][MAX_TYPE_INDEX];

    static class FigParams {

        private MobLinFun mobilityMG;
        private MobLinFun mobilityEG;

        private TropismFun tropismMG;
        private TropismFun tropismEG;

        private KingAttackFun kingAtt;

        public FigParams(EvalConfig config, String propBaseName) {
            mobilityMG = config.parseFun(propBaseName + "MobMG");
            mobilityEG = config.parseFun(propBaseName + "MobEG");

            tropismMG = config.parseTrFun(propBaseName + "TropismMG");
            tropismEG = config.parseTrFun(propBaseName + "TropismEG");

            kingAtt = config.parseKAFun(propBaseName + "KingAttack");
        }
    }

    private FigParams[] figParams = new FigParams[6];

    public ParameterizedMobilityEvaluation(EvalConfig config) {

        figParams[FT_KNIGHT] = new FigParams(config, "knight");
        figParams[FT_BISHOP] = new FigParams(config, "bishop");
        figParams[FT_ROOK] = new FigParams(config, "rook");
        figParams[FT_QUEEN] = new FigParams(config, "queen");
        figParams[FT_KING] = new FigParams(config, "king");

        rookOpen = config.getPosIntProp("rookOpen");
        rookHalf = config.getPosIntProp("rookHalf");
        earlyQueenPenalty = config.getPosIntProp("earlyQueenPenalty");

        /* trapped and blocked pieces */
        kingBlocksRookPenalty = config.getPosIntProp("kingBlocksRookPenalty");
        blockCentralPawnPenalty = config.getPosIntProp("blockCentralPawnPenalty");
        bishopTrappedA7Penalty = config.getPosIntProp("bishopTrappedA7Penalty");
        bishopTrappedA6Penalty = config.getPosIntProp("bishopTrappedA6Penalty");
        knightTrappedA8Penalty = config.getPosIntProp("knightTrappedA8Penalty");
        knightTrappedA7Penalty = config.getPosIntProp("knightTrappedA7Penalty");

        c3KnightPenalty = config.getPosIntProp("c3KnightPenalty");

        returningBishop = config.getPosIntProp("returningBishop");

    }

    private void eval(BitBoard bitBoard) {

        clear();

        // calc mobility of each piece.
        // we do not count sqares which are attached by enemy pawns.
        BitChessBoard bb = bitBoard.getBoard();

        eval(bb, Color.WHITE);
        eval(bb, Color.BLACK);

    }

    private void clear() {
        for (int c = 0; c < 2; c++) {
            for (int t = 0; t < MAX_TYPE_INDEX; t++) {
                results[c][t] = 0;
            }
        }
    }

    public void eval(BitChessBoard bb, Color side) {

        Color xside = side.invert();

        long ownFigsMask = bb.getColorMask(side);
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;
        long occupancy = ownFigsMask | opponentFigsMask;

        long oppKingMask = bb.getPieceSet(FT_KING, xside);
        long oppKingZone = createKingZoneMask(oppKingMask, xside);
        int oppKingPos = Long.numberOfTrailingZeros(oppKingMask);

        // opponents pawn attacs. we exclude fields under opponents pawn attack from our mobility
        long oppPawnAttacs = createOpponentPawnAttacs(bb, side);
        long noOppPawnAttacs = ~oppPawnAttacs;

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            long connectivity = attacks & ownFigsMask;
            int tropism = getTropism(bishop, oppKingPos);

            countFigureVals(FT_BISHOP, side, mobility, captures, connectivity, kingZoneAttacs, tropism);

            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);

            long knightAttack = BB.getKnightAttacs(knight);

            long moves = knightAttack & ~ownFigsMask;
            long captures = moves & opponentFigsMask;
            long mobility = moves & ~captures & noOppPawnAttacs;
            long kingZoneAttacs = knightAttack & oppKingZone;
            long connectivity = knightAttack & ownFigsMask;
            int tropism = getTropism(knight, oppKingPos);

            countFigureVals(FT_KNIGHT, side, mobility, captures, connectivity, kingZoneAttacs, tropism);

            knightBB &= knightBB - 1;
        }

        long ownPawns = bb.getPieceSet(FT_PAWN, side);
        long oppPawns = bb.getPieceSet(FT_PAWN, xside);

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long attacks = MagicBitboards.genRookAttacs(rook, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long connectivity = attacks & ownFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            int tropism = getTropism(rook, oppKingPos);

            countFigureVals(FT_ROOK, side, mobility, captures, connectivity, kingZoneAttacs, tropism);

            rookOpenFiles(side, rook, oppKingPos, ownPawns, oppPawns);

            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);

            long attacksRook = MagicBitboards.genRookAttacs(queen, occupancy);
            long attacksBishop = MagicBitboards.genBishopAttacs(queen, occupancy);
            long attacks = attacksRook | attacksBishop;
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            long connectivity = attacks & ownFigsMask;
            int tropism = getTropism(queen, oppKingPos);

            countFigureVals(FT_QUEEN, side, mobility, captures, connectivity, kingZoneAttacs, tropism);

            evalEarlyDevelopedQueen(queenBB, bishopBB, knightBB, side);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long kingAttack = BB.getKingAttacs(king);

        long moves = kingAttack & ~ownFigsMask;
        long captures = moves & opponentFigsMask;
        long mobility = moves & ~captures & noOppPawnAttacs;
        long kingZoneAttacs = kingAttack & oppKingZone;
        long connectivity = kingAttack & ownFigsMask;
        int tropism = getTropism(king, oppKingPos);

        countFigureVals(FT_KING, side, mobility, captures, connectivity, kingZoneAttacs, tropism);

        blockedPieces(bb, side);
    }

    private void evalEarlyDevelopedQueen(long queenBB, long bishopBB, long knightBB, Color side) {

        /**************************************************************************
         *  A queen should not be developed too early                              *
         **************************************************************************/

        if (earlyQueenPenalty > 0) {
            if ((side == WHITE && (queenBB & WHITE_QUEEN_DEVELOPED_MASK) != 0)) {

                results[side.ordinal()][POSITIONAL_THEMES] -=
                        (bitCount(WHITE_BISHOPS_STARTPOS & bishopBB) + bitCount(WHITE_KNIGHT_STARTPOS & knightBB))
                                * earlyQueenPenalty;
            } else if ((side == BLACK && (queenBB & BLACK_QUEEN_DEVELOPED_MASK) != 0)) {

                results[side.ordinal()][POSITIONAL_THEMES] -=
                        (bitCount(BLACK_BISHOPS_STARTPOS & bishopBB) + bitCount(BLACK_KNIGHT_STARTPOS & knightBB))
                                * earlyQueenPenalty;
            }

        }
    }

    public static long createKingZoneMask(long oppKingMask, Color xside) {
        long frontMask = xside == WHITE ? kingAttacks(nortOne(oppKingMask)) : kingAttacks(soutOne(oppKingMask));
        return kingAttacks(oppKingMask) | frontMask;
    }

    private void rookOpenFiles(Color side, int rook, int otherKing, long ownPawns, long oppPawns) {
        long rookMask = 1L << rook;

        long fileFilled = BB.fileFill(rookMask);
        boolean ownPawnOnFile = (fileFilled & ownPawns) != 0;
        boolean oppPawnOnFile = (fileFilled & oppPawns) != 0;

        // fully open file:
        if (!ownPawnOnFile && !oppPawnOnFile) {
            results[side.ordinal()][MOBILITY_MG] += rookOpen;
            results[side.ordinal()][MOBILITY_EG] += rookOpen;
            if (Tools.colDistance(rook, otherKing) < 2) {
                results[side.ordinal()][KING_ATT_WEIGHT] += 1;
            }
        } else if (!ownPawnOnFile && oppPawnOnFile) {
            // half open:
            results[side.ordinal()][MOBILITY_MG] += rookHalf;
            results[side.ordinal()][MOBILITY_EG] += rookHalf;
            if (Tools.colDistance(rook, otherKing) < 2) {
                results[side.ordinal()][KING_ATT_WEIGHT] += 2;
            }
        }

    }

    /**
     * Creates opponents pawn attacs.
     *
     * @param bb
     * @param side
     * @return
     */
    public static long createOpponentPawnAttacs(BitChessBoard bb, Color side) {
        long otherPawns = side == WHITE ? bb.getPieceSet(FT_PAWN, BLACK) : bb.getPieceSet(FT_PAWN, WHITE);

        if (side == WHITE) {
            long capturesEast = BB.bPawnWestAttacks(otherPawns);
            long capturesWest = BB.bPawnEastAttacks(otherPawns);
            return capturesEast | capturesWest;
        } else {
            long capturesEast = BB.wPawnWestAttacks(otherPawns);
            long capturesWest = BB.wPawnEastAttacks(otherPawns);
            return capturesEast | capturesWest;
        }
    }

    private void countFigureVals(byte figureType, Color side,
            long mobility,
            long captures,
            long connectivity,
            long kingZoneAttacs,
            int tropism) {

        FigParams params = figParams[figureType];

        // mobility count is mobility to empty fields as well as captures of enemy pieces:
        int mobCount = bitCount(mobility) + bitCount(captures);
        int kingAttCount = bitCount(kingZoneAttacs);

        int iSide = side.ordinal();
        results[iSide][MOBILITY_MG] += params.mobilityMG.calc(mobCount);
        results[iSide][MOBILITY_EG] += params.mobilityEG.calc(mobCount);
        results[iSide][KING_ATT_COUNT] += kingAttCount;
        results[iSide][KING_ATT_WEIGHT] += params.kingAtt.calc(kingAttCount);

        results[iSide][TROPISM_MG] += params.tropismMG.calc(tropism);
        results[iSide][TROPISM_EG] += params.tropismEG.calc(tropism);

        //        detailedResults[side.ordinal()][figureType][CAPTURES] += weights.dotProduct(captures, side) * 2;
        //        detailedResults[side.ordinal()][figureType][CONNECTIVITY] += weights.dotProduct(connectivity, side) / 2;

    }

    /******************************************************************************
     *                             Pattern detection                               *
     ******************************************************************************/

    void blockedPieces(BitChessBoard bb, Color side) {

        Color oppo = side.invert();

        long ownFigsMask = bb.getColorMask(side);
        long opponentFigsMask = bb.getColorMask(oppo);
        long empty = ~ownFigsMask & ~opponentFigsMask;
        long occupancy = ownFigsMask | opponentFigsMask;

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        long pawnBB = bb.getPieceSet(FT_PAWN, side);
        long pawnOppoBB = bb.getPieceSet(FT_PAWN, oppo);
        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        long kingBB = bb.getPieceSet(FT_KING, side);
        long rookBB = bb.getPieceSet(FT_ROOK, side);

        // Fieldwrapper fw=new fieldMapper(side);
        // fm.c1.isSet(bb)

        // central pawn blocked, bishop hard to develop
        if (Fields.C1.isSet(bishopBB, side)
                && Fields.D2.isSet(pawnBB, side)
                && Fields.D3.isSet(occupancy, side))
            results[side.ordinal()][BLOCKAGES] -= blockCentralPawnPenalty;

        if (F1.isSet(bishopBB, side)
                && E2.isSet(pawnBB, side)
                && Fields.E3.isSet(occupancy, side))
            results[side.ordinal()][BLOCKAGES] -= blockCentralPawnPenalty;

        // trapped knight
        if (Fields.A8.isSet(knightBB, side)
                && (Fields.A7.isSet(pawnOppoBB, side) || Fields.C7.isSet(pawnOppoBB, side)))
            results[side.ordinal()][BLOCKAGES] -= knightTrappedA8Penalty;

        if (Fields.H8.isSet(knightBB, side)
                && (Fields.H7.isSet(pawnOppoBB, side) || Fields.F7.isSet(pawnOppoBB, side)))
            results[side.ordinal()][BLOCKAGES] -= knightTrappedA8Penalty;

        if (Fields.A7.isSet(knightBB, side)
                && Fields.A6.isSet(pawnOppoBB, side)
                && Fields.B7.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= knightTrappedA7Penalty;

        if (Fields.H7.isSet(knightBB, side)
                && Fields.H6.isSet(pawnOppoBB, side)
                && Fields.G7.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= knightTrappedA7Penalty;

        // todo this stmt seems not to be symmetric...?   But the test seem to be wrong with mirroring the game queenside/kingside...
        // knight blocking queenside pawns
        if (Fields.C3.isSet(knightBB, side)
                && Fields.C2.isSet(pawnBB, side)
                && Fields.D2.isSet(pawnBB, side)
                && !Fields.E4.isSet(pawnBB, side))
            results[side.ordinal()][BLOCKAGES] -= c3KnightPenalty;

        // trapped bishop
        if (Fields.A7.isSet(bishopBB, side)
                && Fields.B6.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA7Penalty;

        if (Fields.H7.isSet(bishopBB, side)
                && Fields.G6.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA7Penalty;

        if (Fields.B8.isSet(bishopBB, side)
                && Fields.C7.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA7Penalty;

        if (Fields.G8.isSet(bishopBB, side)
                && Fields.F7.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA7Penalty;

        if (Fields.A6.isSet(bishopBB, side)
                && Fields.B5.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA6Penalty;

        if (Fields.H6.isSet(bishopBB, side)
                && Fields.G5.isSet(pawnOppoBB, side))
            results[side.ordinal()][BLOCKAGES] -= bishopTrappedA6Penalty;

        // bishop on initial sqare supporting castled king
        if (Fields.F1.isSet(bishopBB, side)
                && Fields.G1.isSet(kingBB, side))
            results[side.ordinal()][BLOCKAGES] += returningBishop;

        if (Fields.C1.isSet(bishopBB, side)
                && Fields.B1.isSet(kingBB, side))
            results[side.ordinal()][BLOCKAGES] += returningBishop;

        // uncastled king blocking own rook
        if ((Fields.F1.isSet(kingBB, side) || Fields.G1.isSet(kingBB, side))
                && (Fields.H1.isSet(rookBB, side) || Fields.G1.isSet(rookBB, side)))
            results[side.ordinal()][BLOCKAGES] -= kingBlocksRookPenalty;

        if ((Fields.C1.isSet(kingBB, side) || Fields.B1.isSet(kingBB, side))
                && (Fields.A1.isSet(rookBB, side) || Fields.B1.isSet(rookBB, side)))
            results[side.ordinal()][BLOCKAGES] -= kingBlocksRookPenalty;

    }

    /**
     * Weights the distance to the king from -7 to +6.
     * +6 == nearest to the king. -7 fartest from king.
     *
     * @param sq1
     * @param sq2
     * @return
     */
    private int getTropism(int sq1, int sq2) {
        return 7 - Tools.manhattanDistance(sq1, sq2);
    }

    @Override
    public void eval(EvalResult result, BitBoard bitBoard) {
        eval(bitBoard);

        result.midGame += (results[IWHITE][MOBILITY_MG] - results[IBLACK][MOBILITY_MG]);
        result.endGame += (results[IWHITE][MOBILITY_EG] - results[IBLACK][MOBILITY_EG]);

        result.midGame += (results[IWHITE][TROPISM_MG] - results[IBLACK][TROPISM_MG]);
        result.endGame += (results[IWHITE][TROPISM_EG] - results[IBLACK][TROPISM_EG]);

        /**************************************************************************
         *  Merge king attack score. We don't apply this value if there are less   *
         *  than two attackers or if the attacker has no queen.                    *
         **************************************************************************/

        if (results[IWHITE][KING_ATT_COUNT] < 2 || bitBoard.getBoard().getQueensCount(BitChessBoard.nWhite) == 0)
            results[IWHITE][KING_ATT_WEIGHT] = 0;
        if (results[IBLACK][KING_ATT_COUNT] < 2 || bitBoard.getBoard().getQueensCount(BitChessBoard.nBlack) == 0)
            results[IBLACK][KING_ATT_WEIGHT] = 0;

        result.result +=
                (SAFETYTABLE[results[IWHITE][KING_ATT_WEIGHT]] - SAFETYTABLE[results[IBLACK][KING_ATT_WEIGHT]]);

        result.result +=
                (results[IWHITE][POSITIONAL_THEMES] - results[IBLACK][POSITIONAL_THEMES]);

        result.result +=
                (results[IWHITE][BLOCKAGES] - results[IBLACK][BLOCKAGES]);
    }
}
