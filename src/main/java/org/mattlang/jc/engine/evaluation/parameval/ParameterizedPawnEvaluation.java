package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.engine.evaluation.Tools.fileOf;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedThreatsEvaluation.readCombinedConfigVal;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
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
public class ParameterizedPawnEvaluation implements EvalComponent {

    public static final String PAWN_SHIELD_2 = "pawnShield2";
    public static final String PAWN_SHIELD_3 = "pawnShield3";

    public static final String DOUBLE_PAWN_PENALTY_MG = "doublePawnPenaltyMg";
    public static final String DOUBLE_PAWN_PENALTY_EG = "doublePawnPenaltyEg";

    public static final String ATTACKED_PAWN_PENALTY_MG = "attackedPawnPenaltyMg";
    public static final String ATTACKED_PAWN_PENALTY_EG = "attackedPawnPenaltyEg";

    public static final String ISOLATED_PAWN_PENALTY_MG = "isolatedPawnPenaltyMg";
    public static final String ISOLATED_PAWN_PENALTY_EG = "isolatedPawnPenaltyEg";

    public static final String BACKWARDED_PAWN_PENALTY_MG = "backwardedPawnPenaltyMg";
    public static final String BACKWARDED_PAWN_PENALTY_EG = "backwardedPawnPenaltyEg";

    public static final String PAWN_CONFIG_SUB_DIR = "pawn";

    public static final String WEAK_PAWN_FILE_MG = "weakPawnMg.csv";
    public static final String WEAK_PAWN_FILE_EG = "weakPawnEg.csv";
    public static final String BLOCKED_PAWN_FILE_MG = "blockedPawnMg.csv";
    public static final String BLOCKED_PAWN_FILE_EG = "blockedPawnEg.csv";
    public static final String PASSED_PAWN_FILE_MG = "passedPawnMg.csv";
    public static final String PASSED_PAWN_FILE_EG = "passedPawnEg.csv";
    public static final String PROTECTED_PASSER_CSV_MG = "protectedPasserMg.csv";
    public static final String PROTECTED_PASSER_CSV_EG = "protectedPasserEg.csv";
    public static final String PROTECTED_CSV_MG = "protectedPawnMg.csv";
    public static final String PROTECTED_CSV_EG = "protectedPawnEg.csv";
    public static final String NEIGHBOUR_CSV_MG = "neighbourPawnMg.csv";
    public static final String NEIGHBOUR_CSV_EG = "neighbourPawnEg.csv";

    private final Pattern weakPawnPstMgEg;

    private final Pattern blockedPawnPstMgEg;

    private final Pattern passedPawnPstMgEg;

    private final Pattern protectedPasserPstMgEg;

    private final Pattern protectedPstMgEg;

    private final Pattern neighbourPstMgEg;

    @Setter
    private int shield2 = 10;
    @Setter
    private int shield3 = 5;

    private int doublePawnPenaltyMgEg;
    private ChangeableMgEgScore doublePawnPenaltyScore;

    private int attackedPawnPenaltyMgEg;
    private ChangeableMgEgScore attackedPawnPenaltyScore;

    private int isolatedPawnPenaltyMgEg;
    private ChangeableMgEgScore isolatedPawnPenaltyScore;

    private int backwardedPawnPenaltyMgEg;
    private ChangeableMgEgScore backwardedPawnPenaltyScore;

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

    @Setter
    private PawnCache pawnCache = PawnCache.EMPTY_CACHE;

    public ParameterizedPawnEvaluation(boolean forTuning, boolean caching, EvalConfig config) {
        this.forTuning = forTuning;
        this.caching = caching;

        shield2 = config.getPosIntProp(PAWN_SHIELD_2);
        shield3 = config.getPosIntProp(PAWN_SHIELD_3);
        doublePawnPenaltyScore = readCombinedConfigVal(config, DOUBLE_PAWN_PENALTY_MG, DOUBLE_PAWN_PENALTY_EG,
                val -> doublePawnPenaltyMgEg = val);

        attackedPawnPenaltyScore = readCombinedConfigVal(config, ATTACKED_PAWN_PENALTY_MG, ATTACKED_PAWN_PENALTY_EG,
                val -> attackedPawnPenaltyMgEg = val);
        isolatedPawnPenaltyScore = readCombinedConfigVal(config, ISOLATED_PAWN_PENALTY_MG, ISOLATED_PAWN_PENALTY_EG,
                val -> isolatedPawnPenaltyMgEg = val);
        backwardedPawnPenaltyScore =
                readCombinedConfigVal(config, BACKWARDED_PAWN_PENALTY_MG, BACKWARDED_PAWN_PENALTY_EG,
                        val -> backwardedPawnPenaltyMgEg = val);

        weakPawnPstMgEg = loadCombinedPattern(config, WEAK_PAWN_FILE_MG, WEAK_PAWN_FILE_EG);

        blockedPawnPstMgEg = loadCombinedPattern(config, BLOCKED_PAWN_FILE_MG, BLOCKED_PAWN_FILE_EG);

        passedPawnPstMgEg = loadCombinedPattern(config, PASSED_PAWN_FILE_MG, PASSED_PAWN_FILE_EG);

        protectedPstMgEg = loadCombinedPattern(config, PROTECTED_CSV_MG, PROTECTED_CSV_EG);

        neighbourPstMgEg = loadCombinedPattern(config, NEIGHBOUR_CSV_MG, NEIGHBOUR_CSV_EG);

        protectedPasserPstMgEg = loadCombinedPattern(config, PROTECTED_PASSER_CSV_MG, PROTECTED_PASSER_CSV_EG);

        passedPawnEval.configure(config);
    }

    private Pattern loadCombinedPattern(EvalConfig config, String mgFile, String egFile) {
        Pattern patternMg = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + mgFile);
        Pattern patternEg = loadFromFullPath(config.getConfigDir() + PAWN_CONFIG_SUB_DIR + "/" + egFile);
        return Pattern.combine(patternMg, patternEg);
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        int wKingShield = calcWhiteKingShield(bitBoard.getBoard());
        int bKingShield = calcBlackKingShield(bitBoard.getBoard());

        // king shield is only relevant for middle game:
        result.getMgEgScore().addMg(wKingShield - bKingShield);

        if (caching && !forTuning) {
            long pawnHashKey = bitBoard.getPawnZobristHash();
            PawnCacheEntry entry = pawnCache.find(pawnHashKey);
            if (entry == null) {

                int pawnEval = calcPawnEval(bitBoard);
                result.getMgEgScore().add(pawnEval);
                pawnCache.save(pawnHashKey, pawnEval, whitePassers, blackPassers);
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

        long blockedWhitePawns = whitePawns & BB.soutOne(blackPawns);

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

        long blockedBlackPawns = blackPawns & BB.nortOne(whitePawns);

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
        return pawns << 1 & BB.notHFile | pawns >>> 1 & BB.notAFile;
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
}
