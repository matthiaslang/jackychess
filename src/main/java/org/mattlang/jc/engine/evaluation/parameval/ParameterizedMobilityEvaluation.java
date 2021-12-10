package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.functions.KingAttackFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.MobLinFun;
import org.mattlang.jc.engine.evaluation.parameval.functions.TropismFun;

/**
 * Paremeterized Mobility Evaluation.
 */
public class ParameterizedMobilityEvaluation implements EvalComponent {

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

    public static final int MAX_TYPE_INDEX = CONNECTIVITY + 1;

    private final int rookOpen;
    private final int rookHalf;
    /**
     * splitted results by color, figure type, and mobility, captures, connectivity).
     * They are splitted mainly for debugging purpose.
     * Also the bitmasks of all aspect are saved for debugging purpose. Once we are save with it,
     * we should deactive that lot of individual values.
     */
    private long[][][] masks = new long[2][6][MAX_TYPE_INDEX];
    private int[][][] detailedResults = new int[2][6][MAX_TYPE_INDEX];
    private long[][] maskResults = new long[2][MAX_TYPE_INDEX];
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
    }

    private void eval(BitBoard bitBoard) {

        clear();

        // calc mobility of each piece.
        // we do not count sqares which are attached by enemy pawns.
        BitChessBoard bb = bitBoard.getBoard();

        eval(bb, Color.WHITE);
        eval(bb, Color.BLACK);

        aggregate();

    }

    private void clear() {
        for (int c = 0; c < 2; c++) {

            for (int t = 0; t < MAX_TYPE_INDEX; t++) {
                results[c][t] = 0;
                maskResults[c][t] = 0L;
                for (int f = 0; f < 6; f++) {
                    masks[c][f][t] = 0L;
                    detailedResults[c][f][t] = 0;
                }
            }
        }
    }

    private void aggregate() {
        for (int c = 0; c < 2; c++) {
            for (int t = 0; t < MAX_TYPE_INDEX; t++) {
                for (int f = 0; f < 6; f++) {
                    results[c][t] += detailedResults[c][f][t];
                    maskResults[c][t] |= masks[c][f][t];
                }
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
        long oppKingZone = BB.kingAttacks(oppKingMask);
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

    }

    private void rookOpenFiles(Color side, int rook, int otherKing, long ownPawns, long oppPawns) {
        long rookMask = 1L << rook;

        long fileFilled = BB.fileFill(rookMask);
        boolean ownPawnOnFile = (fileFilled & ownPawns) != 0;
        boolean oppPawnOnFile = (fileFilled & oppPawns) != 0;

        // fully open file:
        if (!ownPawnOnFile && !oppPawnOnFile) {
            detailedResults[side.ordinal()][FT_ROOK][MOBILITY_MG] += rookOpen;
            detailedResults[side.ordinal()][FT_ROOK][MOBILITY_EG] += rookOpen;
            if (Tools.colDistance(rook, otherKing)<2){
                detailedResults[side.ordinal()][FT_ROOK][KING_ATT_WEIGHT] += 1;
            }
        } else if (!ownPawnOnFile && oppPawnOnFile) {
            // half open:
            detailedResults[side.ordinal()][FT_ROOK][MOBILITY_MG] += rookHalf;
            detailedResults[side.ordinal()][FT_ROOK][MOBILITY_EG] += rookHalf;
            if (Tools.colDistance(rook, otherKing)<2){
                detailedResults[side.ordinal()][FT_ROOK][KING_ATT_WEIGHT] += 2;
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

        masks[side.ordinal()][figureType][MOBILITY_MG] |= mobility;
        masks[side.ordinal()][figureType][CAPTURES] |= captures;
        masks[side.ordinal()][figureType][CONNECTIVITY] |= connectivity;

        // mobility count is mobility to empty fields as well as captures of enemy pieces:
        int mobCount = Long.bitCount(mobility) + Long.bitCount(captures);
        int kingAttCount = Long.bitCount(kingZoneAttacs);

        detailedResults[side.ordinal()][figureType][MOBILITY_MG] += params.mobilityMG.calc(mobCount);
        detailedResults[side.ordinal()][figureType][MOBILITY_EG] += params.mobilityEG.calc(mobCount);
        detailedResults[side.ordinal()][figureType][KING_ATT_COUNT] += kingAttCount;
        detailedResults[side.ordinal()][figureType][KING_ATT_WEIGHT] += params.kingAtt.calc(kingAttCount);

        detailedResults[side.ordinal()][figureType][TROPISM_MG] += params.tropismMG.calc(tropism);
        detailedResults[side.ordinal()][figureType][TROPISM_EG] += params.tropismEG.calc(tropism);

        //        detailedResults[side.ordinal()][figureType][CAPTURES] += weights.dotProduct(captures, side) * 2;
        //        detailedResults[side.ordinal()][figureType][CONNECTIVITY] += weights.dotProduct(connectivity, side) / 2;

    }

    /**
     * Weights the distance to the king from -7 to +6.
     * +6 == nearest to the king. -7 fartest from king.
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


        //        int score = (results[IWHITE][MOBILITY_MG] - results[IBLACK][MOBILITY_MG]) * who2mov +
        //                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;

        //score += (results[IWHITE][CONNECTIVITY] - results[IBLACK][CONNECTIVITY]) * who2mov +
        //                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;

        //        return score;
    }
}
