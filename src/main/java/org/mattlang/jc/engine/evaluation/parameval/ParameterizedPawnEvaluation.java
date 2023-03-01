package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.engine.evaluation.Tools.fileOf;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * Paremeterized Pawn Evaluation.
 *
 *
 * see also https://www.chessprogramming.org/Pawns_and_Files_(Bitboards)  for bitboard actions.
 */
@Getter
@Setter
public class ParameterizedPawnEvaluation implements EvalComponent {

    public static final String PAWN_SHIELD_2 = "pawnShield2";
    public static final String PAWN_SHIELD_3 = "pawnShield3";
    public static final String DOUBLE_PAWN_PENALTY = "doublePawnPenalty";
    public static final String ATTACKED_PAWN_PENALTY = "attackedPawnPenalty";
    public static final String ISOLATED_PAWN_PENALTY = "isolatedPawnPenalty";
    public static final String BACKWARDED_PAWN_PENALTY = "backwardedPawnPenalty";

    public static final String PAWN_CONFIG_SUB_DIR = "pawn";

    public static final String WEAK_PAWN_FILE = "weakPawn.csv";
    public static final String BLOCKED_PAWN_FILE = "blockedPawn.csv";
    public static final String PASSED_PAWN_FILE = "passedPawn.csv";
    public static final String PROTECTED_PASSER_CSV = "protectedPasser.csv";
    public static final String PROTECTED_CSV = "protectedPawn.csv";
    public static final String NEIGHBOUR_CSV = "neighbourPawn.csv";
    private final Pattern weakPawnPst;
    private final Pattern blockedPawnPst;

    private final Pattern passedPawnPst;

    private final Pattern protectedPasserPst;
    private final Pattern protectedPst;
    private final Pattern neighbourPst;

    private int shield2 = 10;
    private int shield3 = 5;

    private int doublePawnPenalty = 20;
    private int attackedPawnPenalty = 4;
    private int isolatedPawnPenalty = 10;
    private int backwardedPawnPenalty = 10;

    public ParameterizedPawnEvaluation(EvalConfig config) {
        shield2 = config.getPosIntProp(PAWN_SHIELD_2);
        shield3 = config.getPosIntProp(PAWN_SHIELD_3);
        doublePawnPenalty = config.getPosIntProp(DOUBLE_PAWN_PENALTY);
        attackedPawnPenalty = config.getPosIntProp(ATTACKED_PAWN_PENALTY);
        isolatedPawnPenalty = config.getPosIntProp(ISOLATED_PAWN_PENALTY);
        backwardedPawnPenalty = config.getPosIntProp(BACKWARDED_PAWN_PENALTY);

        weakPawnPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + WEAK_PAWN_FILE);
        blockedPawnPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + BLOCKED_PAWN_FILE);
        passedPawnPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + PASSED_PAWN_FILE);
        protectedPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + PROTECTED_CSV);
        neighbourPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + NEIGHBOUR_CSV);
        protectedPasserPst = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + PROTECTED_PASSER_CSV);
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        int wKingShield = calcWhiteKingShield(bitBoard.getBoard());
        int bKingShield = calcBlackKingShield(bitBoard.getBoard());

        int pawnResultWhite = evalPawns(bitBoard, WHITE);
        int pawnResultBlack = evalPawns(bitBoard, BLACK);
        // king shield is only relevant for middle game:
        result.midGame += (wKingShield - bKingShield);

        result.result += (pawnResultWhite - pawnResultBlack);
    }

    private int evalPawns(BoardRepresentation bitBoard, Color color) {
        int result = 0;

        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        long blackPawns = bb.getPawns(BitChessBoard.nBlack);

        long blackPawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, WHITE);
        long whitePawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, BLACK);

        long protectedWhitePawns = whitePawns & whitePawnAttacs;
        long protectedBlackPawns = blackPawns & blackPawnAttacs;

        long whiteNeighbours = getPawnNeighbours(whitePawns);
        long blackNeighbours = getPawnNeighbours(blackPawns);

        long whiteAdvanceAttackedPawns = BB.nortOne(whitePawns) & blackPawnAttacs;
        long blackAdvanceAttackedPawns = BB.soutOne(blackPawns) & whitePawnAttacs;

        //        long attackedWhitePawns = whitePawns & blackPawnAttacs;
        //        long attackedBlackPawns = blackPawns & whitePawnAttacs;

        // blocked rammed pawns:
        //        long blockedWhitePawns = BB.soutOne(blackPawns) & whitePawns;
        //        long blockedBlackPawns = BB.nortOne(whitePawns) & blackPawns;

        //        long whitePassers = BB.wFrontFill(whitePawns) & ~BB.bFrontFill(blackPawns) & whitePawns;
        //        long blackPassers = BB.bFrontFill(blackPawns) & ~BB.wFrontFill(whitePawns) & blackPawns;

        //        long whiteProtectedPassers = protectedWhitePawns & whitePassers;
        //        long blackProtectedPassers = protectedBlackPawns & blackPassers;

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
                boolean hasNeighbour = (whiteNeighbours & pawnMask) != 0;
                boolean isSupported = false; // todo left, right on same rank another pawn or protected?
                boolean isIsolated = ((BB.ADJACENT_FILES[fileOf(pawn)] & whitePawns) == 0);
                boolean isBehindNeighbours = hasNeighbour && (BB.wFrontFill(whiteNeighbours) & pawnMask) == 0;

                // backward pawn: behind its neighbours and cannot be safely advanced:
                boolean isBackward =
                        !isBlocked && isBehindNeighbours && ((BB.nortOne(pawnMask) & whiteAdvanceAttackedPawns) != 0);

                if (isBackward) {
                    result -= backwardedPawnPenalty;
                }

                if (isIsolated) {
                    result -= isolatedPawnPenalty;
                }

                if (isDoubled) {
                    result -= doublePawnPenalty;
                }
                if (isAttacked) {
                    result -= attackedPawnPenalty;
                }

                if (isBlocked){
                    result += blockedPawnPst.getVal(pawn, color);
                }
                if (isProtected){
                    result += protectedPst.getVal(pawn, color);
                }
                if (hasNeighbour){
                    result += neighbourPst.getVal(pawn, color);
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
                boolean hasNeighbour = (blackNeighbours & pawnMask) !=0;
                boolean isSupported = false; // todo left, right on same rank another pawn or protected?
                boolean isIsolated= ((BB.ADJACENT_FILES[fileOf(pawn)] & blackPawns) == 0);
                boolean isBehindNeighbours = hasNeighbour && (BB.bFrontFill(blackNeighbours) & pawnMask) == 0;

                // backward pawn: behind its neighbours and cannot be safely advanced:
                boolean isBackward =
                        !isBlocked && isBehindNeighbours && ((BB.soutOne(pawnMask) & blackAdvanceAttackedPawns) != 0);

                if (isBackward) {
                    result -= backwardedPawnPenalty;
                }

                if (isIsolated) {
                    result -= isolatedPawnPenalty;
                }

                if (isDoubled) {
                    result -= doublePawnPenalty;
                }
                if (isAttacked) {
                    result -= attackedPawnPenalty;
                }

                if (isBlocked){
                    result += blockedPawnPst.getVal(pawn, color);
                }
                if (isProtected){
                    result += protectedPst.getVal(pawn, color);
                }
                if (hasNeighbour){
                    result += neighbourPst.getVal(pawn, color);
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

    public static long getPawnNeighbours(final long pawns) {
        return pawns << 1 & BB.notHFile | pawns >>> 1 & BB.notAFile;
    }
}
