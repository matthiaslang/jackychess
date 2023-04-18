package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Math.min;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_ROOK;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.Tools.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.FloatArrayFunction;
import org.mattlang.jc.material.Material;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	 * 
	 */

	public static final String MULTIPLIER_BLOCKED = "passedPawn.multiplier.blocked";
	public static final String MULTIPLIER_NO_ENEMY_ATTACKS_IN_FRONT = "passedPawn.multiplier.noEnemyAttacksInFront";
	public static final String MULTIPLIER_NEXT_SQUARE_ATTACKED = "passedPawn.multiplier.nextSquareAttacked";
	public static final String MULTIPLIER_NEXT_SQARE_DEFENDED = "passedPawn.multiplier.nextSquareDefended";
	public static final String MULTIPLIER_ENEMY_KING_IN_FRONT = "passedPawn.multiplier.enemyKingInFront";
	public static final String MULTIPLIER_ATTACKED = "passedPawn.multiplier.attacked";
	public static final String MULTIPLIER_DEFENDED_BY_ROOK_FROM_BEHIND =
			"passedPawn.multiplier.defendedByRookFromBehind";
	public static final String MULTIPLIER_ATTACKED_BY_ROOK_FROM_BEHIND =
			"passedPawn.multiplier.attackedByRookFromBehind";

	public static final String PASSED_KING_MULTI = "passedPawn.kingMultiplicators";

	public static final String PASSED_SCORE_EG = "passedPawn.passedScoreEg";

	private static final long PROMOTION_HOT_RANKS_WHITE = BB.rank78;
	private static final long PROMOTION_RANK_WHITE = BB.rank8;
	private static final long PROMOTION_HOT_RANKS_BLACK = BB.rank12;
	private static final long PROMOTION_RANK_BLACK = BB.rank1;

	private float multiplierBlocked = 0.5f;
	private float multiplierNoEnemyAttacksInFront = 1.8f;
	private float multiplierNextSquareAttacked = 1.3f;
	private float multiplierNextSquareDefended = 1.2f;
	private float multiplierEnemyKingInFront = 0.4f;
	private float multiplierAttacked = 0.7f;
	private float multiplierDefendedByRookFromBehind = 1.7f;
	private float multiplierAttackedByRookFromBehind = 0.6f;

	private FloatArrayFunction passedKingMulti = new FloatArrayFunction(
			new float[] { 0, 1.4f, 1.4f, 1.2f, 1.1f, 1.0f, 0.8f, 0.8f });

	private ArrayFunction passedScoreEg = new ArrayFunction(new int[] { 0, 14, 16, 34, 62, 128, 232 });
	public static final int MAX_PROMOTION_DISTANCE = 32000;
	public static final int PROMOTION_BONUS = 350;

	public int calculateScores(final BoardRepresentation board, EvalResult e, long whitePassedPawns,
			long blackPassedPawns) {

		int score = 0;

		int whitePromotionDistance = MAX_PROMOTION_DISTANCE;
		int blackPromotionDistance = MAX_PROMOTION_DISTANCE;

		// white passed pawns
		long passedPawns = whitePassedPawns;
		while (passedPawns != 0) {
			final int index = 63 - Long.numberOfLeadingZeros(passedPawns);

			score += getPassedPawnScore(board, e, index, nWhite);

			if (whitePromotionDistance == MAX_PROMOTION_DISTANCE) {
				whitePromotionDistance = calcPromotionDistance(board, e, index, nWhite);
			}

			// skip all passed pawns at same file
			passedPawns &= ~file_bb_ofSquare(index);
		}

		// black passed pawns
		passedPawns = blackPassedPawns;
		while (passedPawns != 0) {
			final int index = Long.numberOfTrailingZeros(passedPawns);

			score -= getPassedPawnScore(board, e, index, nBlack);

			if (blackPromotionDistance == MAX_PROMOTION_DISTANCE) {
				blackPromotionDistance = calcPromotionDistance(board, e, index, nBlack);
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

	public int getPassedPawnScore(final BoardRepresentation board, EvalResult e, final int index,
			final int color) {

		BitChessBoard bb = board.getBoard();

		int offsetNextSquare = pawn_push(color).getOffset();
		final int nextIndex = index + offsetNextSquare;
		final long square = 1L << index;
		final long maskNextSquare = 1L << nextIndex;
		final long maskPreviousSquare = 1L << (index - offsetNextSquare);
		final long maskFile = file_bb_ofSquare(index);
		final long pawnFrontFilled = pawnFront(square, color);

		//		System.out.println("Square to check" + BB.toStrBoard(square));

		final int enemyColor = 1 - color;
		float multiplier = 1;

		// is piece blocked?
		if ((bb.getPieces() & maskNextSquare) != 0) {
			multiplier *= multiplierBlocked;
		}

		// is next squared attacked?
		long allEnemyAttacks = e.getAttacks(enemyColor, FT_ALL);
		//		System.out.println("Enemy Attacks" + BB.toStrBoard(allEnemyAttacks));

		if ((allEnemyAttacks & maskNextSquare) == 0) {

			// complete path free of enemy attacks?
			if ((pawnFrontFilled & allEnemyAttacks) == 0) {
				multiplier *= multiplierNoEnemyAttacksInFront;
			} else {
				multiplier *= multiplierNextSquareAttacked;
			}
		}

		// is next squared defended?
		if ((e.getAttacks(color, FT_ALL) & maskNextSquare) != 0) {
			multiplier *= multiplierNextSquareDefended;
		}

		// is enemy king in front?
		if ((pawnFrontFilled & bb.getKings(enemyColor)) != 0) {
			multiplier *= multiplierEnemyKingInFront;
		}

		// under attack?
		if (board.getSiteToMove().ordinal() != color && (allEnemyAttacks & square) != 0) {
			multiplier *= multiplierAttacked;
		}

		// defended by rook from behind?
		if ((maskFile & bb.getRooks(color)) != 0 && (e.getAttacks(color, FT_ROOK) & square) != 0
				&& (e.getAttacks(color, FT_ROOK) & maskPreviousSquare) != 0) {
			multiplier *= multiplierDefendedByRookFromBehind;
		}

		// attacked by rook from behind?
		else if ((maskFile & bb.getRooks(enemyColor)) != 0 && (e.getAttacks(enemyColor, FT_ROOK) & square) != 0
				&& (e.getAttacks(enemyColor, FT_ROOK) & maskPreviousSquare) != 0) {
			multiplier *= multiplierAttackedByRookFromBehind;
		}

		int kingPos = Long.numberOfTrailingZeros(bb.getKings(color));
		int enemyKingPos = Long.numberOfTrailingZeros(bb.getKings(enemyColor));
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

	public static int calcPromotionDistance(final BoardRepresentation cb, EvalResult e, final int index,
			final int color) {
		BitChessBoard bb = cb.getBoard();

		final int enemyColor = 1 - color;

		int promotionDistance = promotionDistance(index, color);

		long squareMask = 1L << index;
		int offSet = pawn_push(color).getOffset();
		int nextSquare = index + offSet;
		long nextSquareMask = 1L << nextSquare;

		final long pawnFrontFilled = pawnFront(squareMask, color);

		// check if it cannot be stopped
		if (promotionDistance == 1 && cb.getSiteToMove().ordinal() == color) {
			if ((nextSquareMask & (e.getAttacks(enemyColor, FT_ALL) | bb.getAllPieces())) == 0) {
				if ((squareMask & e.getAttacks(enemyColor, FT_ALL)) == 0) {
					return 1;
				}
			}
		} else if (onlyPawnsOrOneNightOrBishop(bb, cb.getMaterial(), enemyColor)) {

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

			int kingPos = Long.numberOfTrailingZeros(bb.getKings(color));
			int enemyKingPos = Long.numberOfTrailingZeros(bb.getKings(enemyColor));

			long kingAreaPawn = BB.getKingAttacs(index);
			// check if own king is defending the promotion square (including square just below)
			if ((BB.getKingAttacs(kingPos) & kingAreaPawn & hotRanks) != 0) {
				promotionDistance--;
			}

			long promotionSquareMask = pawnFrontFilled & promoRank;
			int promoPos = Long.numberOfTrailingZeros(promotionSquareMask);

			// check distance of enemy king to promotion square
			if (promotionDistance < Math.max(distance(promoPos, enemyKingPos),
					distance(index, enemyKingPos))) {

				if (!cb.getMaterial().hasNonPawnMat(enemyColor)) {
					return promotionDistance;
				}
				long enemyKnights = bb.getKnights(enemyColor);
				if (enemyKnights != 0) {
					// check distance of enemy night
					if (promotionDistance < min(calcKnightDistance(enemyKnights, squareMask),
							calcKnightDistance(enemyKnights, promotionSquareMask))) {
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
							if ((e.getAttacks(enemyColor, FT_ALL) & squareMask) == 0) {
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

	private static boolean onlyPawnsOrOneNightOrBishop(BitChessBoard bb, Material material, int enemyColor) {

		// check to get this from the Material key instead... should be faster...
		return bb.getRooks(enemyColor) == 0 &&
				bb.getQueens(enemyColor) == 0 &&
				bb.getBishops(enemyColor) <= 1 &&
				bb.getKnights(enemyColor) <= 1 &&
				bb.getBishops(enemyColor) + bb.getKnights(enemyColor) <= 1;
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

	public void configure(EvalConfig config) {
		multiplierBlocked = config.getFloatProp(MULTIPLIER_BLOCKED);

		multiplierNoEnemyAttacksInFront = config.getFloatProp(MULTIPLIER_NO_ENEMY_ATTACKS_IN_FRONT);
		multiplierNextSquareAttacked = config.getFloatProp(MULTIPLIER_NEXT_SQUARE_ATTACKED);
		multiplierNextSquareDefended = config.getFloatProp(MULTIPLIER_NEXT_SQARE_DEFENDED);
		multiplierEnemyKingInFront = config.getFloatProp(MULTIPLIER_ENEMY_KING_IN_FRONT);
		multiplierAttacked = config.getFloatProp(MULTIPLIER_ATTACKED);
		multiplierDefendedByRookFromBehind = config.getFloatProp(MULTIPLIER_DEFENDED_BY_ROOK_FROM_BEHIND);
		multiplierAttackedByRookFromBehind = config.getFloatProp(MULTIPLIER_ATTACKED_BY_ROOK_FROM_BEHIND);

		passedKingMulti = config.parseFloatArray(PASSED_KING_MULTI);
		passedScoreEg = config.parseArray(PASSED_SCORE_EG);
	}
}
