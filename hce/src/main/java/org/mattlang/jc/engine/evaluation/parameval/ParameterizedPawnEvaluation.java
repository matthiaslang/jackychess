package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.BB.flipVertical;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.Tools.fileOf;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
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

    @EvalConfigParam(mgEgCombined = true)
    private Pattern weakPawn;

    @EvalConfigParam(mgEgCombined = true)
    private Pattern blockedPawn;

    @EvalConfigParam(mgEgCombined = true)
    private Pattern passedPawn;

    @EvalConfigParam(mgEgCombined = true)
    private Pattern protectedPasser;

    @EvalConfigParam(mgEgCombined = true)
    private Pattern protectedPawn;

    @EvalConfigParam(mgEgCombined = true)
    private Pattern neighbourPawn;

    @EvalConfigParam
    @EvalValueInterval(min = 0, max = 100)
    @Setter
    private int pawnShield2 = 10;

    @EvalConfigParam
    @EvalValueInterval(min = 0, max = 100)
    @Setter
    private int pawnShield3 = 5;

    @EvalConfigParam(mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int doublePawnPenalty;

    @EvalConfigParam(mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int attackedPawnPenalty;

    @EvalConfigParam(mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int isolatedPawnPenalty;

    @EvalConfigParam(mgEgCombined = true)
    @EvalValueInterval(min = 0, max = 100)
    private int backwardedPawnPenalty;

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

    public ParameterizedPawnEvaluation(boolean forTuning, boolean caching) {
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
        long whitePawns = bb.getPawns(nWhite);
        long blackPawns = bb.getPawns(nBlack);
        whitePassers = wPassedPawns(whitePawns, blackPawns);
        blackPassers = bPassedPawns(blackPawns, whitePawns);
    }

    private int evalWhitePawns(BoardRepresentation bitBoard) {
        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(nWhite);
        long blackPawns = bb.getPawns(nBlack);

        return evalWhitePerspectivePawns(whitePawns, blackPawns, whitePassers);
    }

    private int evalWhitePerspectivePawns(long whitePawns, long blackPawns, long whitePassers) {
        int result = 0;

        long blackPawnAttacs = createBlackPawnAttacs(blackPawns);
        long whitePawnAttacs = createWhitePawnAttacs(whitePawns);

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

            if (isBlocked) {
                result += blockedPawn.getValWhite(pawn);
            }
            if (isProtected) {
                result += protectedPawn.getValWhite(pawn);
            }
            if (hasDirectNeighbour) {
                result += neighbourPawn.getValWhite(pawn);
            }

            if (isWeak) {
                result += weakPawn.getValWhite(pawn);
            } else if (isPasser) {
                if ((isProtected || isSupported)) {
                    result += protectedPasser.getValWhite(pawn);
                } else {
                    result += passedPawn.getValWhite(pawn);
                }
            }

            pawns &= pawns - 1;
        }

        return result;
    }

    public static long createBlackPawnAttacs(long blackPawns) {
        long capturesEast = BB.bPawnWestAttacks(blackPawns);
        long capturesWest = BB.bPawnEastAttacks(blackPawns);
        return capturesEast | capturesWest;
    }

    public static long createWhitePawnAttacs(long whitePawns) {
        long capturesEast = BB.wPawnWestAttacks(whitePawns);
        long capturesWest = BB.wPawnEastAttacks(whitePawns);
        return capturesEast | capturesWest;
    }

    private int evalBlackPawns(BoardRepresentation bitBoard) {

        BitChessBoard bb = bitBoard.getBoard();
        long whitePawns = bb.getPawns(nWhite);
        long blackPawns = bb.getPawns(nBlack);

        return evalWhitePerspectivePawns(flipVertical(blackPawns), flipVertical(whitePawns), flipVertical(blackPassers));
    }

    private int calcBlackKingShield(BitChessBoard bb) {

        long kingMask = flipVertical(bb.getPieceSet(FT_KING, nBlack));
        long pawnsMask = flipVertical(bb.getPawns(nBlack));
        return calcWhitePerspectiveKingShield(kingMask, pawnsMask);
    }

    private int calcWhiteKingShield(BitChessBoard bb) {

        long kingMask = bb.getPieceSet(FT_KING, nWhite);
        long pawnsMask = bb.getPawns(nWhite);
        return calcWhitePerspectiveKingShield(kingMask, pawnsMask);

    }

    private int calcWhitePerspectiveKingShield(long kingMask, long pawnsMask) {

        int result = 0;
        /* king on kingside F-H: */
        if ((kingMask & BB.FGH_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.FGH_on_rank2);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.FGH_on_rank3);

            result += shieldCountOnRank2 * pawnShield2 + shieldCountOnRank3 * pawnShield3;
        } else if ((kingMask & BB.ABC_File) != 0) {
            int shieldCountOnRank2 = bitCount(pawnsMask & BB.ABC_on_rank2);
            int shieldCountOnRank3 = bitCount(pawnsMask & BB.ABC_on_rank3);

            result += shieldCountOnRank2 * pawnShield2 + shieldCountOnRank3 * pawnShield3;
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
}
