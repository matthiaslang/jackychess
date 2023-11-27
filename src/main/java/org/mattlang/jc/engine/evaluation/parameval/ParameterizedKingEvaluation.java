package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.Tools.*;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;
import static org.mattlang.jc.engine.evaluation.parameval.KingZoneMasks.getKingZoneMask;

import java.util.function.Consumer;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.File;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.Getter;

/**
 * Parameterized King Evaluation.
 */
@Getter
public class ParameterizedKingEvaluation implements EvalComponent {

    public static final String KING_CONFIG_SUB_DIR = "king";

    public static final String KING_SHELTER_KING_FILE_MG_CSV = "kingShelterKingFileMg.csv";
    public static final String KING_SHELTER_KING_FILE_EG_CSV = "kingShelterKingFileEg.csv";

    public static final String KING_SHELTER_ADJACENT_FILE_MG_CSV = "kingShelterAdjacentFileMg.csv";
    public static final String KING_SHELTER_ADJACENT_FILE_EG_CSV = "kingShelterAdjacentFileEg.csv";

    public static final String KING_STORM_BLOCKED_MG_CSV = "kingStormBlockedMg.csv";
    public static final String KING_STORM_BLOCKED_EG_CSV = "kingStormBlockedEg.csv";

    public static final String KING_STORM_NON_BLOCKED_MG_CSV = "kingStormNonBlockedMg.csv";
    public static final String KING_STORM_NON_BLOCKED_EG_CSV = "kingStormNonBlockedEg.csv";

    private final Pattern kingShelterKingFileMgEg;
    private final Pattern kingShelterAdjacentFileMgEg;

    private final MgEgArrayFunction safetyShelterKingFile;
    private final MgEgArrayFunction safetyShelterAdjacentFile;
    private final Pattern kingStormBlockedMgEg;
    private final Pattern kingStormNonBlockedMgEg;
    private final MgEgArrayFunction safetyStormBlocked;
    private final MgEgArrayFunction safetyStormNonBlocked;
    private final MgEgArrayFunction kingPawnFileProximity;
    private final MgEgArrayFunction kingDefenders;
    private final ChangeableMgEgScore safetyAttackValue;
    private final ChangeableMgEgScore safetyWeakSquaresValue;
    private final ChangeableMgEgScore safetyNoEnemyQueensValue;
    private final ChangeableMgEgScore safetySafeQueenCheckValue;
    private final ChangeableMgEgScore safetySafeRookCheckValue;
    private final ChangeableMgEgScore safetySafeBishopCheckValue;
    private final ChangeableMgEgScore safetySafeKnightCheckValue;
    private final ChangeableMgEgScore safetyAdjustmentValue;

    private int safetySafeQueenCheck;
    private int safetySafeRookCheck;
    private int safetySafeBishopCheck;
    private int safetySafeKnightCheck;
    private int safetyAdjustment;
    private int safetyNoEnemyQueens;
    private int safetyWeakSquares;
    private int safetyAttack;

    public ParameterizedKingEvaluation(boolean forTuning, EvalConfig config) {

        kingShelterKingFileMgEg =
                loadCombinedPattern(config, KING_SHELTER_KING_FILE_MG_CSV, KING_SHELTER_KING_FILE_EG_CSV);
        kingShelterAdjacentFileMgEg =
                loadCombinedPattern(config, KING_SHELTER_ADJACENT_FILE_MG_CSV, KING_SHELTER_ADJACENT_FILE_EG_CSV);

        kingStormBlockedMgEg =
                loadCombinedPattern(config, KING_STORM_BLOCKED_MG_CSV, KING_STORM_BLOCKED_EG_CSV);
        kingStormNonBlockedMgEg =
                loadCombinedPattern(config, KING_STORM_NON_BLOCKED_MG_CSV, KING_STORM_NON_BLOCKED_EG_CSV);

        safetyShelterKingFile = new MgEgArrayFunction(config, "king.safetyShelterKingFile");
        safetyShelterAdjacentFile = new MgEgArrayFunction(config, "king.safetyShelterAdjacentFile");

        safetyStormBlocked = new MgEgArrayFunction(config, "king.safetyStormBlocked");
        safetyStormNonBlocked = new MgEgArrayFunction(config, "king.safetyStormNonBlocked");
        kingPawnFileProximity = new MgEgArrayFunction(config, "king.kingPawnFileProximity");
        kingDefenders = new MgEgArrayFunction(config, "king.kingDefenders");

        safetyAttackValue = readCombinedConfigVal(config, "king.safetyAttackValue", val -> safetyAttack = val);

        safetyWeakSquaresValue =
                readCombinedConfigVal(config, "king.safetyWeakSquares", val -> safetyWeakSquares = val);

        safetyNoEnemyQueensValue =
                readCombinedConfigVal(config, "king.safetyNoEnemyQueens", val -> safetyNoEnemyQueens = val);

        safetySafeQueenCheckValue =
                readCombinedConfigVal(config, "king.safetySafeQueenCheck", val -> safetySafeQueenCheck = val);

        safetySafeRookCheckValue =
                readCombinedConfigVal(config, "king.safetySafeRookCheck", val -> safetySafeRookCheck = val);

        safetySafeBishopCheckValue =
                readCombinedConfigVal(config, "king.safetySafeBishopCheck", val -> safetySafeBishopCheck = val);

        safetySafeKnightCheckValue =
                readCombinedConfigVal(config, "king.safetySafeKnightCheck", val -> safetySafeKnightCheck = val);

        safetyAdjustmentValue = readCombinedConfigVal(config, "king.safetyAdjustment", val -> safetyAdjustment = val);

    }

    public static ChangeableMgEgScore readCombinedConfigVal(EvalConfig config, String propKey,
            Consumer<Integer> changeListener) {
        String mgPropKey = propKey + "Mg";
        String egPropKey = propKey + "Eg";
        return ParameterizedThreatsEvaluation.readCombinedConfigVal(config, mgPropKey, egPropKey, changeListener);

    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        if (result.getPawnEntry() != null) {
            for (int i = 0; i < 2; i++) {
                result.pkeval = result.getPawnEntry().pkeval;
                result.pksafety[i] = result.getPawnEntry().pksafety[i];
            }

        } else {
            // This needs to be done after pawn evaluation but before king safety evaluation
            int pkEvalWhite = evaluateKingsPawns(result, bitBoard, nWhite);
            int pkEvalBlack = evaluateKingsPawns(result, bitBoard, nBlack);
            result.pkeval = pkEvalWhite - pkEvalBlack;
        }

        result.getMgEgScore().add(result.pkeval);

        result.getMgEgScore().add(evaluateKings(result, bitBoard, nWhite) - evaluateKings(result, bitBoard, nBlack));
    }

    private int evaluateKingsPawns(EvalResult result, BoardRepresentation board, int color) {

        int US = color, THEM = Color.invert(color);

        BitChessBoard bb = board.getBoard();
        long myPawns = bb.getPawns(US);
        long enemyPawns = bb.getPawns(THEM);

        int kingSq = Long.numberOfTrailingZeros(bb.getKings(US));

        // Evaluate based on the number of files between our King and the nearest
        // file-wise pawn. If there is no pawn, kingPawnFileDistance() returns the
        // same distance for both sides causing this evaluation term to be neutral
        int dist = BB.kingPawnFileDistance(bb.getPawns(), kingSq);
        int pkeval = kingPawnFileProximity.calc(dist);

        // Evaluate King Shelter & King Storm threat by looking at the file of our King,
        // as well as the adjacent files. When looking at pawn distances, we will use a
        // distance of 7 to denote a missing pawn, since distance 7 is not possible otherwise.
        int adjacentMinFile = calcMinAdjFile(kingSq);
        int adjacentMaxFile = calcMaxAdjFile(kingSq);

        for (int file = adjacentMinFile; file <= adjacentMaxFile; file++) {

            // Find closest friendly pawn at or above our King on a given file
            int ourDist = calcPawnKingRankDelta(myPawns, file, US, kingSq);

            // Find closest enemy pawn at or above our King on a given file
            int theirDist = calcPawnKingRankDelta(enemyPawns, file, US, kingSq);

            // Evaluate King Shelter using pawn distance. Use separate evaluation
            // depending on the file, and if we are looking at the King's file
            boolean isKingFile = file == fileOf(kingSq);
            pkeval += evalKingShelter(isKingFile, file, ourDist);

            // Update the Shelter Safety
            result.pksafety[US] += evalSafetyShelter(isKingFile, ourDist);

            // Evaluate King Storm using enemy pawn distance. Use a separate evaluation
            // depending on the file, and if the opponent's pawn is blocked by our own
            boolean blocked = ourDist != 7 && (ourDist == theirDist - 1);

            pkeval += evalKingStorm(blocked, file, theirDist);

            // Update the Storm Safety
            result.pksafety[US] += evalSafetyStorm(blocked, theirDist);

        }
        return pkeval;
    }

    public static int calcMaxAdjFile(int kingSq) {
        return min(BB.FILE_NB - 1, fileOf(kingSq) + 1);
    }

    public static int calcMinAdjFile(int kingSq) {
        return max(0, fileOf(kingSq) - 1);
    }

    private int evalSafetyShelter(boolean isKingFile, int ourDist) {
        if (isKingFile) {
            return safetyShelterKingFile.calc(ourDist);
        } else {
            return safetyShelterAdjacentFile.calc(ourDist);
        }
    }

    private int evalKingShelter(boolean kingFile, int file, int ourDist) {
        if (kingFile) {
            return kingShelterKingFileMgEg.getRawVal(file, ourDist);
        } else {
            return kingShelterAdjacentFileMgEg.getRawVal(file, ourDist);
        }
    }

    private int evalSafetyStorm(boolean blocked, int theirDist) {
        if (blocked) {
            return safetyStormBlocked.calc(theirDist);
        } else {
            return safetyStormNonBlocked.calc(theirDist);
        }
    }

    private int evalKingStorm(boolean blocked, int file, int theirDist) {
        if (blocked) {
            return kingStormBlockedMgEg.getRawVal(file, theirDist);
        } else {
            return kingStormNonBlockedMgEg.getRawVal(file, theirDist);
        }
    }

    /**
     * Find closest pawn Rank distance at or above our King on a given file
     *
     * @param pawns
     * @param file
     * @param color
     * @param kingSq
     * @return
     */
    public static int calcPawnKingRankDelta(long pawns, int file, int color, int kingSq) {
        long ours = pawns & File.file(file).fileMask & Tools.forward_ranks_bb(color, kingSq);
        int ourDist = ours == 0 ? 7 : Math.abs(rankOf(kingSq) - rankOf(backmost(color, ours)));
        return ourDist;
    }

    int evaluateKings(EvalResult evalResult, BoardRepresentation bitBoard, int color) {

        int US = color;
        int THEM = Color.invert(color);

        int count, safety, eval = 0;

        BitChessBoard bb = bitBoard.getBoard();
        long enemyQueens = bb.getQueens(THEM);

        long defenders = bb.getPawns(US) | bb.getKnights(US) | bb.getBishops(US);

        int kingSq = Long.numberOfTrailingZeros(bb.getKings(US));

        long kingZone = getKingZoneMask(US, kingSq);

        // Bonus for our pawns and minors sitting within our king area
        eval += kingDefenders.calc(bitCount(defenders & kingZone));

        // Perform King Safety when we have two attackers, or
        // one attacker with a potential for a Queen attacker
        int kingAttackersCount = bitCount(evalResult.getAttacks(US, FigureConstants.FT_KING));
        if (kingAttackersCount > 1 - bitCount(enemyQueens)) {

            // Weak squares are attacked by the enemy, defended no more
            // than once and only defended by our Queens or our King
            long weak = evalResult.getAttacks(THEM, FT_ALL)
                    & ~evalResult.getDoubleAttacks(US)
                    & (~evalResult.getAttacks(US, FT_ALL)
                    | evalResult.getAttacks(US, FT_QUEEN)
                    | evalResult.getAttacks(US, FT_KING));

            // Usually the King Area is 9 squares. Scale are attack counts to account for
            // when the king is in an open area and expects more attacks, or the opposite
            int scaledAttackCounts = (int) 9.0 * kingAttackersCount / bitCount(kingZone);

            // Safe target squares are defended or are weak and attacked by two.
            // We exclude squares containing pieces which we cannot capture.
            long safe = ~bb.getColorMask(THEM)
                    & (~evalResult.getAttacks(US, FT_ALL) | (weak & evalResult.getDoubleAttacks(THEM)));

            // Find square and piece combinations which would check our King
            long occupied = bb.getPieces();
            long knightThreats = BB.getKnightAttacs(kingSq);
            long bishopThreats = MagicBitboards.genBishopAttacs(kingSq, occupied);
            long rookThreats = MagicBitboards.genRookAttacs(kingSq, occupied);
            long queenThreats = bishopThreats | rookThreats;

            // Identify if there are pieces which can move to the checking squares safely.
            // We consider forking a Queen to be a safe check, even with our own Queen.
            long knightChecks = knightThreats & safe & evalResult.getAttacks(THEM, FT_KNIGHT);
            long bishopChecks = bishopThreats & safe & evalResult.getAttacks(THEM, FT_BISHOP);
            long rookChecks = rookThreats & safe & evalResult.getAttacks(THEM, FT_ROOK);
            long queenChecks = queenThreats & safe & evalResult.getAttacks(THEM, FT_QUEEN);

            safety = 0; // todo use king attackers weight as base?? ei -> kingAttackersWeight[US];

            safety += safetyAttack * scaledAttackCounts
                    + safetyWeakSquares * bitCount(weak & kingZone)
                    + safetyNoEnemyQueens * (enemyQueens == 0 ? 1 : 0)
                    + safetySafeQueenCheck * bitCount(queenChecks)
                    + safetySafeRookCheck * bitCount(rookChecks)
                    + safetySafeBishopCheck * bitCount(bishopChecks)
                    + safetySafeKnightCheck * bitCount(knightChecks)
                    + evalResult.pksafety[US]
                    + safetyAdjustment;

            // Convert safety to an MG and EG score
            //            mg = ScoreMG(safety), eg = ScoreEG(safety);
            //            eval += MakeScore(-mg * MAX(0, mg) / 720, -MAX(0, eg) / 20);

            // do we need to scale "safety" first?
            eval += safety;

        }

        return eval;
    }

    private Pattern loadCombinedPattern(EvalConfig config, String mgFile, String egFile) {
        Pattern patternMg = loadFromFullPath(config.getConfigDir() + KING_CONFIG_SUB_DIR + "/" + mgFile);
        Pattern patternEg = loadFromFullPath(config.getConfigDir() + KING_CONFIG_SUB_DIR + "/" + egFile);
        return Pattern.combine(patternMg, patternEg);
    }
}
