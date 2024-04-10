package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.engine.evaluation.Tools.fileOf;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * Paremeterized Pawn Evaluation.
 * <p>
 * <p>
 * see also https://www.chessprogramming.org/Pawns_and_Files_(Bitboards)  for bitboard actions.
 */
@Getter
@EvalConfigurable(prefix = "pawn")
public final class ParameterizedPawnEvaluation implements EvalComponent {

    @EvalConfigParam(configName = "weakPawn", mgEgCombined = true)
    private Pattern weakPawnPstMgEg;

    @EvalConfigParam(configName = "blockedPawn", mgEgCombined = true)
    private Pattern blockedPawnPstMgEg;

    @EvalConfigParam(configName = "passedPawn", mgEgCombined = true)
    private Pattern passedPawnPstMgEg;

    @EvalConfigParam(configName = "protectedPasser", mgEgCombined = true)
    private Pattern protectedPasserPstMgEg;

    @EvalConfigParam(configName = "protectedPawn", mgEgCombined = true)
    private Pattern protectedPstMgEg;

    @EvalConfigParam(configName = "neighbourPawn", mgEgCombined = true)
    private Pattern neighbourPstMgEg;

    @EvalConfigParam(configName = "pawnShield2")
    @EvalValueInterval(min = 0, max = 100)
    @Setter
    private int shield2 = 10;

    @EvalConfigParam(configName = "pawnShield3")
    @EvalValueInterval(min = 0, max = 100)
    @Setter
    private int shield3 = 5;

    @EvalConfigParam(configName = "doublePawnPenalty", mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int doublePawnPenaltyMgEg;

    @EvalConfigParam(configName = "attackedPawnPenalty", mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int attackedPawnPenaltyMgEg;

    @EvalConfigParam(configName = "isolatedPawnPenalty", mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int isolatedPawnPenaltyMgEg;

    @EvalConfigParam(configName = "backwardedPawnPenalty", mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int backwardedPawnPenaltyMgEg;

    private PassedPawnEval passedPawnEval = new PassedPawnEval();

    /**
     * transient calculated passers field during evaluation.
     */
    private long whitePassers;

    /**
     * transient calculated passers field during evaluation.
     */
    private long blackPassers;

    private final boolean forTuning;
    private boolean caching;

    public ParameterizedPawnEvaluation(boolean forTuning, boolean caching, EvalConfig config) {
        this.forTuning = forTuning;
        this.caching = caching;

    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        int wKingShield = calcWhiteKingShield(bitBoard.getBoard());
        int bKingShield = calcBlackKingShield(bitBoard.getBoard());

        // king shield is only relevant for middle game:
        result.getMgEgScore().addMg(wKingShield - bKingShield);

        if (caching && !forTuning) {
            PawnCacheEntry entry = result.getPawnEntry();
            if (entry == null) {
                int pawnEval = calcPawnEval(bitBoard);
                result.getMgEgScore().add(pawnEval);
                result.pawnEval = pawnEval;
                result.whitePassers = whitePassers;
                result.blackPassers = blackPassers;
            } else {
                // use cached score:
                result.getMgEgScore().add(entry.score);

                // use cached passers:
                whitePassers = entry.whitePassers;
                blackPassers = entry.blackPassers;

            }
        } else {
            result.getMgEgScore().add(calcPawnEval(bitBoard));
        }

        result.getMgEgScore().addEg(passedPawnEval.calculateScores(bitBoard, result, whitePassers, blackPassers));
    }

    private int calcPawnEval(BoardRepresentation bitBoard) {

        // calc passers first, since they are needed by the following evals:
        calcPassers(bitBoard.getBoard());

        int pawnResultWhite = evalWhitePawns(bitBoard);
        int pawnResultBlack = evalBlackPawns(bitBoard);

        return pawnResultWhite - pawnResultBlack;
    }

    private void calcPassers(BitChessBoard bb) {
        long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        long blackPawns = bb.getPawns(BitChessBoard.nBlack);
        whitePassers = wPassedPawns(whitePawns, blackPawns);
        blackPassers = bPassedPawns(blackPawns, whitePawns);
    }

    private int evalWhitePawns(BoardRepresentation bitBoard) {
        int result = 0;

        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        long blackPawns = bb.getPawns(BitChessBoard.nBlack);

        long blackPawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, WHITE);
        long whitePawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, BLACK);

        long protectedWhitePawns = whitePawns & whitePawnAttacs;

        long whiteDirectNeighbours = getPawnNeighbours(whitePawns);

        long whiteNeighboursFrontFilled = BB.wFrontFill(whiteDirectNeighbours);

        long whiteAdvanceAttackedPawns = BB.nortOne(whitePawns) & blackPawnAttacs;

        long blockedWhitePawns = calcBlockedWhitePawns(whitePawns, blackPawns);

        //        long attackedWhitePawns = whitePawns & blackPawnAttacs;
        //        long attackedBlackPawns = blackPawns & whitePawnAttacs;

        //        long whiteProtectedPassers = protectedWhitePawns & whitePassers;
        //        long blackProtectedPassers = protectedBlackPawns & blackPassers;

        long pawns = whitePawns;
        while (pawns != 0) {
            final int pawn = Long.numberOfTrailingZeros(pawns);
            long pawnMask = 1L << pawn;
            long pawnFrontFilled = BB.wFrontFill(pawnMask);

            boolean isAttacked = (pawnMask & blackPawnAttacs) != 0;
            boolean isPasser = (whitePassers & pawnMask) != 0;
            boolean isWeak = (pawnFrontFilled & blackPawnAttacs) != 0;
            boolean isProtected = (pawnMask & protectedWhitePawns) != 0;
            boolean isBlocked = (blockedWhitePawns & pawnMask) != 0;
            boolean isDoubled = Long.bitCount(pawnFrontFilled & whitePawns) > 1;
            boolean hasDirectNeighbour = (whiteDirectNeighbours & pawnMask) != 0;
            boolean isSupported = hasDirectNeighbour;
            boolean isIsolated = ((BB.ADJACENT_FILES[fileOf(pawn)] & whitePawns) == 0);
            boolean hasNeighbour = !isIsolated;
            boolean isBehindNeighbours = hasNeighbour && (whiteNeighboursFrontFilled & pawnMask) == 0;

            // backward pawn: behind its neighbours and cannot be safely advanced:
            boolean isBackward =
                    !isBlocked && isBehindNeighbours && ((BB.nortOne(pawnMask) & whiteAdvanceAttackedPawns) != 0);

            if (isBackward) {
                result -= backwardedPawnPenaltyMgEg;
            }

            if (isIsolated) {
                result -= isolatedPawnPenaltyMgEg;
            }

            if (isDoubled) {
                result -= doublePawnPenaltyMgEg;
            }
            if (isAttacked) {
                result -= attackedPawnPenaltyMgEg;
            }

            if (isBlocked) {
                result += blockedPawnPstMgEg.getValWhite(pawn);
            }
            if (isProtected) {
                result += protectedPstMgEg.getValWhite(pawn);
            }
            if (hasDirectNeighbour) {
                result += neighbourPstMgEg.getValWhite(pawn);
            }

            if (isWeak) {
                result += weakPawnPstMgEg.getValWhite(pawn);
            } else if (isPasser) {
                if ((isProtected || isSupported)) {
                    result += protectedPasserPstMgEg.getValWhite(pawn);
                } else {
                    result += passedPawnPstMgEg.getValWhite(pawn);
                }
            }

            pawns &= pawns - 1;
        }

        return result;
    }

    private int evalBlackPawns(BoardRepresentation bitBoard) {
        int result = 0;

        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        long blackPawns = bb.getPawns(BitChessBoard.nBlack);

        long blackPawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, WHITE);
        long whitePawnAttacs = ParameterizedMobilityEvaluation.createOpponentPawnAttacs(bb, BLACK);

        long protectedBlackPawns = blackPawns & blackPawnAttacs;

        long blackDirectNeighbours = getPawnNeighbours(blackPawns);

        long blackNeighboursFrontFilled = BB.bFrontFill(blackDirectNeighbours);

        long blackAdvanceAttackedPawns = BB.soutOne(blackPawns) & whitePawnAttacs;

        long blockedBlackPawns = calcBlockedBlackPawns(blackPawns, whitePawns);

        //        long attackedWhitePawns = whitePawns & blackPawnAttacs;
        //        long attackedBlackPawns = blackPawns & whitePawnAttacs;

        //        long whiteProtectedPassers = protectedWhitePawns & whitePassers;
        //        long blackProtectedPassers = protectedBlackPawns & blackPassers;

        long pawns = blackPawns;
        while (pawns != 0) {
            final int pawn = Long.numberOfTrailingZeros(pawns);
            long pawnMask = 1L << pawn;
            long pawnFrontFilled = BB.bFrontFill(pawnMask);

            boolean isAttacked = (pawnMask & whitePawnAttacs) != 0;
            boolean isPasser = (blackPassers & pawnMask) != 0;
            boolean isWeak = (pawnFrontFilled & whitePawnAttacs) != 0;
            boolean isProtected = (pawnMask & protectedBlackPawns) != 0;
            boolean isBlocked = (blockedBlackPawns & pawnMask) != 0;
            boolean isDoubled = Long.bitCount(pawnFrontFilled & blackPawns) > 1;
            boolean hasDirectNeighbour = (blackDirectNeighbours & pawnMask) != 0;
            boolean isSupported = hasDirectNeighbour;
            boolean isIsolated = ((BB.ADJACENT_FILES[fileOf(pawn)] & blackPawns) == 0);
            boolean hasNeighbour = !isIsolated;
            boolean isBehindNeighbours = hasNeighbour && (blackNeighboursFrontFilled & pawnMask) == 0;

            // backward pawn: behind its neighbours and cannot be safely advanced:
            boolean isBackward =
                    !isBlocked && isBehindNeighbours && ((BB.soutOne(pawnMask) & blackAdvanceAttackedPawns) != 0);

            if (isBackward) {
                result -= backwardedPawnPenaltyMgEg;
            }

            if (isIsolated) {
                result -= isolatedPawnPenaltyMgEg;
            }

            if (isDoubled) {
                result -= doublePawnPenaltyMgEg;
            }
            if (isAttacked) {
                result -= attackedPawnPenaltyMgEg;
            }

            if (isBlocked) {
                result += blockedPawnPstMgEg.getValBlack(pawn);
            }
            if (isProtected) {
                result += protectedPstMgEg.getValBlack(pawn);
            }
            if (hasDirectNeighbour) {
                result += neighbourPstMgEg.getValBlack(pawn);
            }

            if (isWeak) {
                result += weakPawnPstMgEg.getValBlack(pawn);
            } else if (isPasser) {
                if ((isProtected || isSupported)) {
                    result += protectedPasserPstMgEg.getValBlack(pawn);
                } else {
                    result += passedPawnPstMgEg.getValBlack(pawn);
                }
            }

            pawns &= pawns - 1;

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
        return (pawns << 1 | pawns >>> 1) & BB.notHFile & BB.notAFile;
    }

    public static long wPassedPawns(long wpawns, long bpawns) {
        long allFrontSpans = BB.bFrontSpans(bpawns);
        allFrontSpans |= BB.eastOne(allFrontSpans)
                | BB.westOne(allFrontSpans);
        return wpawns & ~allFrontSpans;
    }

    public static long bPassedPawns(long bpawns, long wpawns) {
        long allFrontSpans = BB.wFrontSpans(wpawns);
        allFrontSpans |= BB.eastOne(allFrontSpans)
                | BB.westOne(allFrontSpans);
        return bpawns & ~allFrontSpans;
    }

    /**
     * calc blocked (rammed) white pawns.
     *
     * @param whitePawns
     * @param blackPawns
     * @return
     */
    public static long calcBlockedWhitePawns(long whitePawns, long blackPawns) {
        return whitePawns & BB.soutOne(blackPawns);
    }

    /**
     * calc blocked (rammed) black pawns.
     *
     * @param blackPawns
     * @param whitePawns
     * @return
     */
    public static long calcBlockedBlackPawns(long blackPawns, long whitePawns) {
        return blackPawns & BB.nortOne(whitePawns);
    }
}
