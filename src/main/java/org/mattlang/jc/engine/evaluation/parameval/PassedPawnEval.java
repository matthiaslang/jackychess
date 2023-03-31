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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassedPawnEval {

	private static long PROMOTION_HOT_RANKS_WHITE = BB.rank78;
	private static long PROMOTION_RANK_WHITE = BB.rank8;
	private static long PROMOTION_HOT_RANKS_BLACK = BB.rank12;
	private static long PROMOTION_RANK_BLACK = BB.rank1;

	private float multiplierBlocked = 0.5f;
	private float multiplierNoEnemyAttacksInFront = 1.8f;
	private float multiplierNextSquareAttacked = 1.3f;
	private float multiplierNextSquareDefended = 1.2f;
	private float multiplierEnemyKingInFront = 0.4f;
	private float multiplierAttacked = 0.7f;
	private float multiplierDefendedByRookFromBehind = 1.7f;
	private float multiplierAttackedByRookFromBehind = 0.6f;

	public static final float[] PASSED_KING_MULTI = { 0, 1.4f, 1.4f, 1.2f, 1.1f, 1.0f, 0.8f, 0.8f };

	public static final int[] PASSED_SCORE_EG = { 0, 14, 16, 34, 62, 128, 232 };
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
			final int index = /*63 -*/ Long.numberOfLeadingZeros(passedPawns);

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

	private int getPassedPawnScore(final BoardRepresentation board, EvalResult e, final int index,
			final int color) {

		BitChessBoard bb = board.getBoard();

		int offsetNextSquare = pawn_push(color).getOffset();
		final int nextIndex = index + offsetNextSquare;
		final long square = 1L << index;
		final long maskNextSquare = 1L << nextIndex;
		final long maskPreviousSquare = 1L << (index - offsetNextSquare);
		final long maskFile = file_bb_ofSquare(index);
		final long pawnFrontFilled = color == nWhite ? BB.wFrontFill(square) : BB.bFrontFill(square);

		final int enemyColor = 1 - color;
		float multiplier = 1;

		// is piece blocked?
		if ((bb.getPieces() & maskNextSquare) != 0) {
			multiplier *= multiplierBlocked;
		}

		// is next squared attacked?
		long allEnemyAttacks = e.getAttacks(enemyColor, FT_ALL);
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
		multiplier *= PASSED_KING_MULTI[Tools.distance(kingPos, index)];
		multiplier *= PASSED_KING_MULTI[8 - Tools.distance(enemyKingPos, index)];

		int rankDistancePromotion = promotionDistance(index, color);

		return (int) (PASSED_SCORE_EG[rankDistancePromotion] * multiplier);
	}

	private static int promotionDistance(int index, int color) {
		int rankOf = Tools.rankOf(index);
		// todo unbedingt testen, ob das so stimmt oder invertiert sein muss:
		int rankDistancePromotion = color == nWhite ? rankOf : 8 - rankOf;
		return rankDistancePromotion;
	}

	private static int calcPromotionDistance(final BoardRepresentation cb, EvalResult e, final int index,
			final int color) {
		BitChessBoard bb = cb.getBoard();

		final int enemyColor = 1 - color;

		int promotionDistance = promotionDistance(index, color);

		long squareMask = 1L << index;
		int offSet = pawn_push(color).getOffset();
		int nextSquare = index + offSet;
		long nextSquareMask = 1L << nextSquare;

		final long pawnFrontFilled = color == nWhite ? BB.wFrontFill(squareMask) : BB.bFrontFill(squareMask);

		// check if it cannot be stopped
		if (promotionDistance == 1 && cb.getSiteToMove().ordinal() == color) {
			if ((nextSquareMask & (e.getAttacks(enemyColor, FT_ALL) | bb.getAllPieces())) == 0) {
				if ((squareMask & e.getAttacks(enemyColor, FT_ALL)) == 0) {
					return 1;
				}
			}
		} else if (onlyPawnsOrOneNightOrBishop(bb, enemyColor)) {

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

	private static boolean onlyPawnsOrOneNightOrBishop(BitChessBoard bb, int enemyColor) {
		// check to get this from the Material key instead... should be faster...

		return bb.getRooks(enemyColor) == 0 &&
				bb.getQueens(enemyColor) == 0 &&
				(bb.getBishops(enemyColor) == 1 && bb.getKnights(enemyColor) == 0
						|| bb.getBishops(enemyColor) == 0 && bb.getKnights(enemyColor) == 1);
	}

}
