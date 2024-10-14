package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Math.min;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_ROOK;
import static org.mattlang.jc.board.Tools.*;
import static org.mattlang.jc.material.Material.*;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Tools;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.material.Material;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EvalConfigurable(prefix = "passedPawn")
public final class PassedPawnEval {

    /**
     * Example config:
     * passedPawn.multiplier.blocked=0.5
     * passedPawn.multiplier.noEnemyAttacksInFront=1.8
     * passedPawn.multiplier.nextSquareAttacked=1.3
     * passedPawn.multiplier.nextSquareDefended=1.2
     *
     * passedPawn.multiplier.enemyKingInFront=0.4
     * passedPawn.multiplier.attacked=0.7
     * passedPawn.multiplier.defendedByRookFromBehind=1.7
     * passedPawn.multiplier.attackedByRookFromBehind=0.6
     *
     * passedPawn.kingMultiplicators=0, 1.4, 1.4, 1.2, 1.1, 1.0, 0.8, 0.8
     * passedPawn.passedScoreEg=0, 14, 16, 34, 62, 128, 232
     */

    private static final long PROMOTION_HOT_RANKS_WHITE = BB.rank78;
    private static final long PROMOTION_RANK_WHITE = BB.rank8;
    private static final long PROMOTION_HOT_RANKS_BLACK = BB.rank12;
    private static final long PROMOTION_RANK_BLACK = BB.rank1;

    @EvalConfigParam(name = "multiplier.blocked")
    private float multiplierBlocked = 0.5f;

    @EvalConfigParam(name = "multiplier.noEnemyAttacksInFront")
    private float multiplierNoEnemyAttacksInFront = 1.8f;

    @EvalConfigParam(name = "multiplier.nextSquareAttacked")
    private float multiplierNextSquareAttacked = 1.3f;

    @EvalConfigParam(name = "multiplier.nextSquareDefended")
    private float multiplierNextSquareDefended = 1.2f;

    @EvalConfigParam(name = "multiplier.enemyKingInFront")
    private float multiplierEnemyKingInFront = 0.4f;

    @EvalConfigParam(name = "multiplier.attacked")
    private float multiplierAttacked = 0.7f;

    @EvalConfigParam(name = "multiplier.defendedByRookFromBehind")
    private float multiplierDefendedByRookFromBehind = 1.7f;

    @EvalConfigParam(name = "multiplier.attackedByRookFromBehind")
    private float multiplierAttackedByRookFromBehind = 0.6f;

    @EvalConfigParam(name = "kingMultiplicators")
    private FloatArrayFunction passedKingMulti = new FloatArrayFunction(
            new float[] { 0, 1.4f, 1.4f, 1.2f, 1.1f, 1.0f, 0.8f, 0.8f });

    @EvalConfigParam(name = "passedScoreEg")
    @EvalValueInterval(min = 0, max = 400)
    private ArrayFunction passedScoreEg = new ArrayFunction(new int[] { 0, 14, 16, 34, 62, 128, 232 });

    public static final int MAX_PROMOTION_DISTANCE = 32000;
    public static final int PROMOTION_BONUS = 350;

    /**
     * precalced pawn fronts.
     */
    private static long[][] pawnFronts = new long[64][2];

    static {
        for (int square = 0; square < 64; square++) {
            long squareMask = 1L << square;
            for (int color = 0; color < 2; color++) {
                pawnFronts[square][color] = pawnFront(squareMask, color);
            }
        }
    }

    /**
     * precalc knight distance:
     */
    private static int[][] knightDistance = new int[64][64];

    static {
        for (int sq1 = 0; sq1 < 64; sq1++) {
            for (int sq2 = 0; sq2 < 64; sq2++) {
                knightDistance[sq1][sq2] = Tools.knightDistance(sq1, sq2);
            }
        }
    }

    private int enemyColor;
    private long allPieces;
    private long allEnemyAttacks;
    private long allOwnAttacks;
    private long enemyKings;
    private int kingPos;
    private int enemyKingPos;
    private long kingAttacks;
    private long rooks;
    private long enemyRooks;
    private long enemyRookAttacks;
    private long rookAttacks;

    public int calculateScores(final BoardRepresentation board, EvalResult e, long whitePassedPawns,
            long blackPassedPawns) {

        int score = 0;

        int whitePromotionDistance = MAX_PROMOTION_DISTANCE;
        int blackPromotionDistance = MAX_PROMOTION_DISTANCE;

        // white passed pawns
        initTempVars(board, e, nWhite);
        long passedPawns = whitePassedPawns;
        while (passedPawns != 0) {
            final int index = 63 - Long.numberOfLeadingZeros(passedPawns);

            score += getPassedPawnScore(board, index, nWhite);

            if (whitePromotionDistance == MAX_PROMOTION_DISTANCE) {
                whitePromotionDistance = calcPromotionDistance(board, index, nWhite);
            }

            // skip all passed pawns at same file
            passedPawns &= ~file_bb_ofSquare(index);
        }

        // black passed pawns
        initTempVars(board, e, nBlack);
        passedPawns = blackPassedPawns;
        while (passedPawns != 0) {
            final int index = Long.numberOfTrailingZeros(passedPawns);

            score -= getPassedPawnScore(board, index, nBlack);

            if (blackPromotionDistance == MAX_PROMOTION_DISTANCE) {
                blackPromotionDistance = calcPromotionDistance(board, index, nBlack);
            }

            // skip all passed pawns at same file
            passedPawns &= ~file_bb_ofSquare(index);
        }

        if (whitePromotionDistance < blackPromotionDistance - 1) {
            score += PROMOTION_BONUS;
        } else if (whitePromotionDistance > blackPromotionDistance + 1) {
            score -= PROMOTION_BONUS;
        }

        return score;
    }

    private void initTempVars(final BoardRepresentation board, EvalResult e, int color) {
        BitChessBoard bb = board.getBoard();

        enemyColor = 1 - color;

        allPieces = bb.getPieces();
        allEnemyAttacks = e.getAttacks(enemyColor, FT_ALL);
        allOwnAttacks = e.getAttacks(color, FT_ALL);

        enemyKings = bb.getKings(enemyColor);
        kingPos = Long.numberOfTrailingZeros(bb.getKings(color));
        enemyKingPos = Long.numberOfTrailingZeros(enemyKings);

        kingAttacks = BB.getKingAttacs(kingPos);

        rooks = bb.getRooks(color);
        rookAttacks = e.getAttacks(color, FT_ROOK);

        enemyRooks = bb.getRooks(enemyColor);
        enemyRookAttacks = e.getAttacks(enemyColor, FT_ROOK);

    }

    private int getPassedPawnScore(final BoardRepresentation board, final int index, final int color) {

        int offsetNextSquare = pawn_push(color).getOffset();
        final int nextIndex = index + offsetNextSquare;
        final long square = 1L << index;
        final long maskNextSquare = 1L << nextIndex;
        final long maskPreviousSquare = 1L << (index - offsetNextSquare);
        final long maskFile = file_bb_ofSquare(index);
        final long pawnFrontFilled = pawnFronts[index][color];

        //		System.out.println("Square to check" + BB.toStrBoard(square));

        float multiplier = 1;

        // is piece blocked?
        if ((allPieces & maskNextSquare) != 0) {
            multiplier *= multiplierBlocked;
        }

        // is next squared attacked?
        if ((allEnemyAttacks & maskNextSquare) == 0) {

            // complete path free of enemy attacks?
            if ((pawnFrontFilled & allEnemyAttacks) == 0) {
                multiplier *= multiplierNoEnemyAttacksInFront;
            } else {
                multiplier *= multiplierNextSquareAttacked;
            }
        }

        // is next squared defended?
        if ((allOwnAttacks & maskNextSquare) != 0) {
            multiplier *= multiplierNextSquareDefended;
        }

        // is enemy king in front?
        if ((pawnFrontFilled & enemyKings) != 0) {
            multiplier *= multiplierEnemyKingInFront;
        }

        // under attack?
        if (board.getSiteToMove().ordinal() != color && (allEnemyAttacks & square) != 0) {
            multiplier *= multiplierAttacked;
        }

        // defended by rook from behind?
        if ((maskFile & rooks) != 0 && (rookAttacks & square) != 0
                && (rookAttacks & maskPreviousSquare) != 0) {
            multiplier *= multiplierDefendedByRookFromBehind;
        }

        // attacked by rook from behind?
        else if ((maskFile & enemyRooks) != 0 && (enemyRookAttacks & square) != 0
                && (enemyRookAttacks & maskPreviousSquare) != 0) {
            multiplier *= multiplierAttackedByRookFromBehind;
        }

        // king tropism
        multiplier *= passedKingMulti.calc(Tools.distance(kingPos, index));
        multiplier *= passedKingMulti.calc(8 - Tools.distance(enemyKingPos, index));

        int rankDistancePromotion = promotionDistance(index, color);

        return (int) (passedScoreEg.calc(rankDistancePromotion) * multiplier);
    }

    private static int promotionDistance(int index, int color) {
        int rankOf = Tools.rankOf(index);
        int rankDistancePromotion = color == nWhite ? 7 - rankOf : rankOf;
        return rankDistancePromotion;
    }

    public int calcPromotionDistance(final BoardRepresentation cb, final int index,
            final int color) {
        BitChessBoard bb = cb.getBoard();

        final int enemyColor = 1 - color;

        int promotionDistance = promotionDistance(index, color);

        long squareMask = 1L << index;
        int offSet = pawn_push(color).getOffset();
        int nextSquare = index + offSet;
        long nextSquareMask = 1L << nextSquare;

        // check if it cannot be stopped
        if (promotionDistance == 1 && cb.getSiteToMove().ordinal() == color) {
            if ((nextSquareMask & (allEnemyAttacks | allPieces)) == 0) {
                return 1;
                //				if ((squareMask & allEnemyAttacks) == 0) {
                //					return 1;
                //				}
            }
        } else if (onlyPawnsOrOneNightOrBishop(cb.getMaterial(), enemyColor)) {

            final long pawnFrontFilled = pawnFronts[index][color];

            // check if it is my turn
            if (cb.getSiteToMove().ordinal() == enemyColor) {
                promotionDistance++;
            }

            // check if own pieces are blocking the path
            if ((bb.getColorMask(color) & pawnFrontFilled) != 0) {
                promotionDistance++;
            }

            long hotRanks = color == nWhite ? PROMOTION_HOT_RANKS_WHITE : PROMOTION_HOT_RANKS_BLACK;
            long promoRank = color == nWhite ? PROMOTION_RANK_WHITE : PROMOTION_RANK_BLACK;

            long kingAreaPawn = BB.getKingAttacs(index);
            // check if own king is defending the promotion square (including square just below)
            if ((kingAttacks & kingAreaPawn & hotRanks) != 0) {
                promotionDistance--;
            }

            long promotionSquareMask = pawnFrontFilled & promoRank;
            int promoPos = Long.numberOfTrailingZeros(promotionSquareMask);

            // check distance of enemy king to promotion square
            if (promotionDistance < Math.max(distance(promoPos, enemyKingPos), distance(index, enemyKingPos))) {

                if (!cb.getMaterial().hasNonPawnMat(enemyColor)) {
                    return promotionDistance;
                }
                long enemyKnights = bb.getKnights(enemyColor);
                if (enemyKnights != 0) {
                    int enemyKnight = Long.numberOfTrailingZeros(enemyKnights);
                    // check distance of enemy night
                    if (promotionDistance < min(knightDistance[enemyKnight][index],
                            knightDistance[enemyKnight][promoPos])) {
                        return promotionDistance;
                    }
                } else {
                    // can bishop stop the passed pawn?

                    // we are one move before promotion:
                    if ((squareMask & hotRanks) != 0) {
                        // and our square color is the square color of the bishop:
                        if (((squareMask & BB.WHITE_SQUARES) == 0) == (
                                (bb.getBishops(enemyColor) & BB.WHITE_SQUARES) == 0)) {
                            // and we are not attacked:
                            if ((allEnemyAttacks & squareMask) == 0) {
                                // then we cant be stopped:
                                return promotionDistance;
                            }
                        }
                    }
                }
            }
        }
        return MAX_PROMOTION_DISTANCE;
    }

    public static boolean onlyPawnsOrOneNightOrBishop(Material material, int enemyColor) {
        if (enemyColor == nWhite) {
            int pieceMat = material.getWhitePieceMat();
            return pieceMat == 0 || pieceMat == W_KNIGHT_VAL || pieceMat == W_BISHOP_VAL;
        } else {
            int pieceMat = material.getBlackPieceMat();
            return pieceMat == 0 || pieceMat == B_KNIGHT_VAL || pieceMat == B_BISHOP_VAL;
        }
    }

    /**
     * Returns a mask with the front squares of the pawn filled excluding its own square. (The path of
     * the pawn till promotion)
     *
     * @param square
     * @param color
     * @return
     */
    public static long pawnFront(long square, int color) {
        return color == nWhite ? BB.wFrontFill(square) & ~square : BB.bFrontFill(square) & ~square;
    }

}
