package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_KING;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * Paremeterized Pawn Evaluation.
 *
 *
 * see also https://www.chessprogramming.org/Pawns_and_Files_(Bitboards)  for bitboard actions.
 */
public class ParameterizedPawnEvaluation implements EvalComponent {

    private final Pattern weakPawnPst;

    private final Pattern passedPawnPst;

    private final Pattern protectedPasserPst;

    private int shield2 = 10;
    private int shield3 = 5;

    private int doublePawnPenalty = 20;
    private int attackedPawnPenalty = 4;

    public ParameterizedPawnEvaluation(EvalConfig config) {
        shield2 = config.getPosIntProp("pawnShield2");
        shield3 = config.getPosIntProp("pawnShield3");
        doublePawnPenalty = config.getPosIntProp("doublePawnPenalty");
        attackedPawnPenalty = config.getPosIntProp("attackedPawnPenalty");

        weakPawnPst = Pattern.loadFromFullPath(config.getConfigDir() + "pawn/weakPawn.csv");
        passedPawnPst = Pattern.loadFromFullPath(config.getConfigDir() + "pawn/passedPawn.csv");
        protectedPasserPst = Pattern.loadFromFullPath(config.getConfigDir() + "pawn/protectedPasser.csv");
    }

    @Override
    public void eval(EvalResult result, BitBoard bitBoard) {

        int wKingShield = calcWhiteKingShield(bitBoard.getBoard());
        int bKingShield = calcBlackKingShield(bitBoard.getBoard());

        int pawnResultWhite = evalPawns(bitBoard, WHITE);
        int pawnResultBlack = evalPawns(bitBoard, BLACK);
        // king shield is only relevant for middle game:
        result.midGame += (wKingShield - bKingShield);

        result.result += (pawnResultWhite - pawnResultBlack);
    }

    private int evalPawns(BitBoard bitBoard, Color color) {
        int result = 0;

        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        long blackPawns = bb.getPawns(BitChessBoard.nBlack);

        long blackPawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, WHITE);
        long whitePawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, BLACK);

        long protectedWhitePawns = whitePawns & whitePawnAttacs;
        long protectedBlackPawns = blackPawns & blackPawnAttacs;

        long attackedWhitePawns = whitePawns & blackPawnAttacs;
        long attackedBlackPawns = blackPawns & whitePawnAttacs;

        // blocked rammed pawns:
        long blockedWhitePawns = BB.soutOne(blackPawns) & whitePawns;
        long blockedBlackPawns = BB.nortOne(whitePawns) & blackPawns;

        long whitePassers = BB.wFrontFill(whitePawns) & ~BB.bFrontFill(blackPawns) & whitePawns;
        long blackPassers = BB.bFrontFill(blackPawns) & ~BB.wFrontFill(whitePawns) & blackPawns;

        long whiteProtectedPassers = protectedWhitePawns & whitePassers;
        long blackProtectedPassers = protectedBlackPawns & blackPassers;

        if (color == WHITE) {
            long pawns = whitePawns;
            while (pawns != 0) {
                final int pawn = Long.numberOfTrailingZeros(pawns);
                long pawnMask = 1L << pawn;

                boolean isAttacked = (pawnMask & blackPawnAttacs) != 0;
                boolean isPasser = (BB.wFrontFill(pawnMask) & ~BB.bFrontFill(blackPawns) & pawnMask) != 0;
                boolean isWeak = (BB.wFrontFill(pawnMask) & blackPawnAttacs) != 0;
                boolean isProtected = (pawnMask & protectedWhitePawns) != 0;
                boolean isBlocked = (BB.soutOne(blackPawns) & pawnMask) != 0;
                boolean isDoubled = Long.bitCount(BB.wFrontFill(pawnMask) & whitePawns) > 1;
                boolean isSupported = false; // todo left, right on same rank another pawn or protected?

                if (isDoubled) {
                    result -= doublePawnPenalty;
                }
                if (isAttacked) {
                    result -= attackedPawnPenalty;
                }

                if (isWeak) {
                    result += weakPawnPst.getVal(pawn, color);
                } else if (isPasser) {
                    if ((isProtected || isSupported)) {
                        result += protectedPasserPst.getVal(pawn, color);
                    } else {
                        result += passedPawnPst.getVal(pawn, color);
                    }
                }

                pawns &= pawns - 1;
            }
        } else {
            long pawns = blackPawns;
            while (pawns != 0) {
                final int pawn = Long.numberOfTrailingZeros(pawns);
                long pawnMask = 1L << pawn;

                boolean isAttacked = (pawnMask & whitePawnAttacs) != 0;
                boolean isPasser = (BB.bFrontFill(pawnMask) & ~BB.wFrontFill(whitePawns) & pawnMask) != 0;
                boolean isWeak = (BB.bFrontFill(pawnMask) & whitePawnAttacs) != 0;
                boolean isProtected = (pawnMask & protectedBlackPawns) != 0;
                boolean isBlocked = (BB.nortOne(whitePawns) & pawnMask) != 0;
                boolean isDoubled = Long.bitCount(BB.bFrontFill(pawnMask) & blackPawns) > 1;
                boolean isSupported = false; // todo left, right on same rank another pawn or protected?

                if (isDoubled) {
                    result -= doublePawnPenalty;
                }
                if (isAttacked) {
                    result -= attackedPawnPenalty;
                }

                if (isWeak) {
                    result += weakPawnPst.getVal(pawn, color);
                } else if (isPasser) {
                    if ((isProtected || isSupported)) {
                        result += protectedPasserPst.getVal(pawn, color);
                    } else {
                        result += passedPawnPst.getVal(pawn, color);
                    }
                }

                pawns &= pawns - 1;

            }
        }
        return result;
    }

    private int calcBlackKingShield(BitChessBoard bb) {
        int result = 0;

        long kingMask = bb.getPieceSet(FT_KING, BLACK);

        long pawnsMask = bb.getPawns(BitChessBoard.nBlack);

        /* king on kingside F-H: */
        if ((kingMask & BB.FGH_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.FGH_on_rank7);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.FGH_on_rank6);

            result += shieldCountOnRank2 * shield2 + shieldCountOnRank3 * shield3;
        } else if ((kingMask & BB.ABC_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.ABC_on_rank7);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.ABC_on_rank6);

            result += shieldCountOnRank2 * shield2 + shieldCountOnRank3 * shield3;
        }

        return result;
    }

    private int calcWhiteKingShield(BitChessBoard bb) {

        int result = 0;

        long kingMask = bb.getPieceSet(FT_KING, WHITE);

        long pawnsMask = bb.getPawns(BitChessBoard.nWhite);

        /* king on kingside F-H: */
        if ((kingMask & BB.FGH_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.FGH_on_rank2);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.FGH_on_rank3);

            result += shieldCountOnRank2 * shield2 + shieldCountOnRank3 * shield3;
        } else if ((kingMask & BB.ABC_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.ABC_on_rank2);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.ABC_on_rank3);

            result += shieldCountOnRank2 * shield2 + shieldCountOnRank3 * shield3;
        }

        return result;
    }
}
