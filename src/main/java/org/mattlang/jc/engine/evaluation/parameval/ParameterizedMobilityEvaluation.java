package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.engine.evaluation.parameval.MobLinFun.parse;

import java.util.Properties;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;

/**
 * Paremeterized Mobility Evaluation.
 */
public class ParameterizedMobilityEvaluation implements EvalComponent {

    public static final int IWHITE = Color.WHITE.ordinal();
    public static final int IBLACK = Color.BLACK.ordinal();

    public static final int MOBILITY_MG = 0;
    public static final int MOBILITY_EG = 1;
    public static final int CAPTURES = 2;
    public static final int CONNECTIVITY = 3;

    public static final int MAX_TYPE_INDEX = CONNECTIVITY + 1;
    /**
     * splitted results by color, figure type, and mobility, captures, connectivity).
     * They are splitted mainly for debugging purpose.
     * Also the bitmasks of all aspect are saved for debugging purpose. Once we are save with it,
     * we should deactive that lot of individual values.
     */
    private long[][][] masks = new long[2][6][MAX_TYPE_INDEX];
    private int[][][] detailedResults = new int[2][6][MAX_TYPE_INDEX];
    public int[][] results = new int[2][MAX_TYPE_INDEX];

    //    private MobLinFun funPawnMG;
    private MobLinFun funKnightMG;
    private MobLinFun funBishopMG;
    private MobLinFun funRookMG;
    private MobLinFun funQueenMG;
    private MobLinFun funKingMG;
    //    private MobLinFun funPawnEG;
    private MobLinFun funKnightEG;
    private MobLinFun funBishopEG;
    private MobLinFun funRookEG;
    private MobLinFun funQueenEG;
    private MobLinFun funKingEG;

    public ParameterizedMobilityEvaluation(Properties properties) {

        funKnightMG = parseFun(properties, "knightMobMG");
        funBishopMG = parseFun(properties, "bishopMobMG");
        funRookMG = parseFun(properties, "rookMobMG");
        funQueenMG = parseFun(properties, "queenMobMG");
        funKingMG = parseFun(properties, "kingMobMG");

        funKnightEG = parseFun(properties, "knightMobEG");
        funBishopEG = parseFun(properties, "bishopMobEG");
        funRookEG = parseFun(properties, "rookMobEG");
        funQueenEG = parseFun(properties, "queenMobEG");
        funKingEG = parseFun(properties, "kingMobEG");
    }

    private MobLinFun parseFun(Properties properties, String propName) {
        try {
            return parse(properties.getProperty(propName));
        } catch (RuntimeException r) {
            throw new IllegalArgumentException("Error parsing Property " + propName, r);
        }
    }

    public void eval(BitBoard bitBoard) {

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
                }
            }
        }
    }

    public void eval(BitChessBoard bb, Color side) {

        Color xside = side.invert();

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;
        long occupancy = ownFigsMask | opponentFigsMask;

        // opponents pawn attacs. we exclude fields under opponents pawn attack from our mobility
        long oppPawnAttacs = createOpponentPawnAttacs(bb, side);
        long noOppPawnAttacs = ~oppPawnAttacs;

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long connectivity = attacks & ownFigsMask;
            countBishop(side, mobility, captures, connectivity);

            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);

            long knightAttack = BB.getKnightAttacs(knight);

            long moves = knightAttack & ~ownFigsMask;
            long captures = moves & opponentFigsMask;
            long mobility = moves & ~captures & noOppPawnAttacs;
            long connectivity = knightAttack & ownFigsMask;

            countKnight(side, mobility, captures, connectivity);

            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long attacks = MagicBitboards.genRookAttacs(rook, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long connectivity = attacks & ownFigsMask;
            countRook(side, mobility, captures, connectivity);

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
            long connectivity = attacks & ownFigsMask;
            countQueen(side, mobility, captures, connectivity);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long kingAttack = BB.getKingAttacs(king);

        long moves = kingAttack & ~ownFigsMask;
        long captures = moves & opponentFigsMask;
        long mobility = moves & ~captures & noOppPawnAttacs;
        long connectivity = kingAttack & ownFigsMask;
        countKing(side, mobility, captures, connectivity);

    }

    /**
     * Creates opponents pawn attacs.
     *
     * @param bb
     * @param side
     * @return
     */
    private long createOpponentPawnAttacs(BitChessBoard bb, Color side) {
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

    private void countKing(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_KING, funKingMG, funKingEG, side, mobility, captures, connectivity);
    }

    private void countQueen(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_QUEEN, funQueenMG, funQueenEG, side, mobility, captures, connectivity);
    }

    private void countRook(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_ROOK, funRookMG, funRookEG, side, mobility, captures, connectivity);
    }

    private void countKnight(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_KNIGHT, funKnightMG, funKnightEG, side, mobility, captures, connectivity);
    }

    private void countBishop(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_BISHOP, funBishopMG, funBishopEG, side, mobility, captures, connectivity);
    }

    private void countFigureVals(byte figureType, MobLinFun mgFun, MobLinFun egFun, Color side, long mobility,
            long captures,
            long connectivity) {
        masks[side.ordinal()][figureType][MOBILITY_MG] |= mobility;
        masks[side.ordinal()][figureType][CAPTURES] |= captures;
        masks[side.ordinal()][figureType][CONNECTIVITY] |= connectivity;

        int mobCount = Long.bitCount(mobility);
        detailedResults[side.ordinal()][figureType][MOBILITY_MG] += mgFun.calc(mobCount);
        detailedResults[side.ordinal()][figureType][MOBILITY_EG] += egFun.calc(mobCount);
        //        detailedResults[side.ordinal()][figureType][CAPTURES] += weights.dotProduct(captures, side) * 2;
        //        detailedResults[side.ordinal()][figureType][CONNECTIVITY] += weights.dotProduct(connectivity, side) / 2;

    }

    @Override
    public void eval(EvalResult result, BitBoard bitBoard, Color who2Move) {
        eval(bitBoard);
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        result.midGame += (results[IWHITE][MOBILITY_MG] - results[IBLACK][MOBILITY_MG]) * who2mov;
        result.endGame += (results[IWHITE][MOBILITY_EG] - results[IBLACK][MOBILITY_EG]) * who2mov;

        //        int score = (results[IWHITE][MOBILITY_MG] - results[IBLACK][MOBILITY_MG]) * who2mov +
        //                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;

        //score += (results[IWHITE][CONNECTIVITY] - results[IBLACK][CONNECTIVITY]) * who2mov +
        //                (results[IWHITE][CAPTURES] - results[IBLACK][CAPTURES]) * who2mov;

        //        return score;
    }
}
