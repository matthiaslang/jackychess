package org.mattlang.jc.engine.evaluation.pstEval2;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * Mobility Evaluation.
 */
public class MobilityEvaluation {

    public static final int[] MATRIX_10 = new int[] {
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
    };

    public static final int[] MATRIX_0 = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };

    public static final int[] MATRIX_15 = new int[] {
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
    };

    public static final int[] MATRIX_5 = new int[] {
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
    };

    public static final int[] MATRIX_1 = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
    };

    public final static Pattern BISHOP_MOBILITY = new Pattern(MATRIX_10);

    public final static Pattern KNIGHT_MOBILITY = new Pattern(MATRIX_10);

    public final static Pattern ROOK_MOBILITY = new Pattern(MATRIX_1);

    public final static Pattern QUEEN_MOBILITY = new Pattern(MATRIX_5);

    public final static Pattern KING_MOBILITY = new Pattern(MATRIX_1);

    public static final int IWHITE = Color.WHITE.ordinal();
    public static final int IBLACK = Color.BLACK.ordinal();

    public static final int MOBILITY = 0;
    public static final int CAPTURES = 1;
    public static final int CONNECTIVITY = 2;
    /**
     * splitted results by color, figure type, and mobility, captures, connectivity).
     * They are splitted mainly for debugging purpose.
     * Also the bitmasks of all aspect are saved for debugging purpose. Once we are save with it,
     * we should deactive that lot of individual values.
     */
    private long[][][] masks = new long[2][6][3];
    private int[][][] detailedResults = new int[2][6][3];
    public int[][] results = new int[2][3];

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

            for (int t = 0; t < 3; t++) {
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
            for (int t = 0; t < 3; t++) {
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
        long noOppPawnAttacs=~oppPawnAttacs;

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
        countFigureVals(FT_KING, KING_MOBILITY, side, mobility, captures, connectivity);
    }

    private void countQueen(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_QUEEN, QUEEN_MOBILITY, side, mobility, captures, connectivity);
    }

    private void countRook(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_ROOK, ROOK_MOBILITY, side, mobility, captures, connectivity);
    }

    private void countKnight(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_KNIGHT, KNIGHT_MOBILITY, side, mobility, captures, connectivity);
    }

    private void countBishop(Color side, long mobility, long captures, long connectivity) {
        countFigureVals(FT_BISHOP, BISHOP_MOBILITY, side, mobility, captures, connectivity);
    }

    private void countFigureVals(byte figureType, Pattern weights, Color side, long mobility, long captures,
            long connectivity) {
        masks[side.ordinal()][figureType][MOBILITY] |= mobility;
        masks[side.ordinal()][figureType][CAPTURES] |= captures;
        masks[side.ordinal()][figureType][CONNECTIVITY] |= connectivity;

        detailedResults[side.ordinal()][figureType][MOBILITY] += weights.dotProduct(mobility, side);
        detailedResults[side.ordinal()][figureType][CAPTURES] += weights.dotProduct(captures, side) * 2;
        detailedResults[side.ordinal()][figureType][CONNECTIVITY] += weights.dotProduct(connectivity, side) / 2;

    }

}
