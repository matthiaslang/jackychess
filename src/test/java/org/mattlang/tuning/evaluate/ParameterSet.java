package org.mattlang.tuning.evaluate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.board.FigureType.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedAdjustmentsEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPawnEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.engine.evaluation.parameval.*;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobFigParams;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobilityEvalResult;
import org.mattlang.tuning.FloatIntervall;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;

/**
 * Bundles all parameters to tune.
 */
public class ParameterSet {

    private static final IntIntervall MOBILITY_VALUE_INTERVAL = new IntIntervall(-500, +500);
    private static final IntIntervall KINGATTACK_VALUE_INTERVAL = new IntIntervall(0, +50);
    public static final IntIntervall PAWN_PARAMS_INTERVALl = new IntIntervall(0, 100);
    public static final FloatIntervall PASSEDPAWN_FLOAT_INTERVALl = new FloatIntervall(0.1f, 3f);

    public static final IntIntervall THREATS_PARAMS_INTERVALl = new IntIntervall(0, 200);
    public static final IntIntervall PASSEDPAWN_SCORE_INTERVALl = new IntIntervall(0, 400);

    /**
     * all parameter groups.
     */
    private ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    /**
     * flat redundant list of all parameters of all groups.
     */
    @Getter
    private ArrayList<TuningParameter> params = new ArrayList<>();

    /**
     * Inits a parameter set by its tuning parameter definitions and with values read from a parameterized
     * evaluation.
     *
     * @param optParams
     * @param parameterizedEvaluation
     */
    public ParameterSet(OptParameters optParams, ParameterizedEvaluation parameterizedEvaluation) {

        if (optParams.isTunePawnEval()) {
            addPawnParameters(parameterizedEvaluation);
        }

        if (optParams.isTunePassedPawnEval()) {
            addPassedPawnParameters(parameterizedEvaluation);
        }

        if (optParams.isTuneAdjustments()) {
            addAdjustmentParameters(parameterizedEvaluation);
        }

        if (optParams.isTuneMaterial()) {
            addMaterialParameters(parameterizedEvaluation);
        }

        if (optParams.isTunePst()) {
            addPstParameters(parameterizedEvaluation);
        }
        ParameterizedMobilityEvaluation mobEval = parameterizedEvaluation.getMobEvaluation();

        if (optParams.isTuneMobility()) {
            addMobilityParameters(parameterizedEvaluation, mobEval);
        }

        if (optParams.isTuneThreats()) {
            addThreatsParameters(parameterizedEvaluation);
        }

        if (optParams.isTunePositional()) {
            addPositionalParameters(parameterizedEvaluation);
        }

        if (optParams.isTuneKingAttack()) {
            addKingAttackParameters(parameterizedEvaluation, mobEval);
        }

        createRedundantParamsList();

    }

    private ParameterSet() {

    }

    private void createRedundantParamsList() {
        params.clear();
        for (TuningParameterGroup group : groups) {
            params.addAll(group.getParameters());
        }
    }

    private void addThreatsParameters(ParameterizedEvaluation parameterizedEvaluation) {

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_MINOR_MG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByMinorMg(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_MINOR_EG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByMinorEg(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_ROOK_MG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByRookMg(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_ROOK_EG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByRookEg(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_KING_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByKingMg(),
                (e, val) -> e.getThreatsEvaluation().setThreatByKingMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_KING_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByKingEg(),
                (e, val) -> e.getThreatsEvaluation().setThreatByKingEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.HANGING_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getHangingMg(),
                (e, val) -> e.getThreatsEvaluation().setHangingMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.HANGING_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getHangingEg(),
                (e, val) -> e.getThreatsEvaluation().setHangingEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(
                new IntegerValueParam(ParameterizedThreatsEvaluation.WEAK_QUEEN_PROTECTION_MG, parameterizedEvaluation,
                        e -> e.getThreatsEvaluation().getWeakQueenProtectionMg(),
                        (e, val) -> e.getThreatsEvaluation().setWeakQueenProtectionMg(val),
                        THREATS_PARAMS_INTERVALl));
        groups.add(
                new IntegerValueParam(ParameterizedThreatsEvaluation.WEAK_QUEEN_PROTECTION_EG, parameterizedEvaluation,
                        e -> e.getThreatsEvaluation().getWeakQueenProtectionEg(),
                        (e, val) -> e.getThreatsEvaluation().setWeakQueenProtectionEg(val),
                        THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.RESTRICTED_PIECE_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getRestrictedPieceMg(),
                (e, val) -> e.getThreatsEvaluation().setRestrictedPieceMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.RESTRICTED_PIECE_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getRestrictedPieceEg(),
                (e, val) -> e.getThreatsEvaluation().setRestrictedPieceEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_PAWN_PUSH_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByPawnPushMg(),
                (e, val) -> e.getThreatsEvaluation().setThreatByPawnPushMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_PAWN_PUSH_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByPawnPushEg(),
                (e, val) -> e.getThreatsEvaluation().setThreatByPawnPushEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_SAFE_PAWN_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatBySafePawnMg(),
                (e, val) -> e.getThreatsEvaluation().setThreatBySafePawnMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.THREAT_BY_SAFE_PAWN_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatBySafePawnEg(),
                (e, val) -> e.getThreatsEvaluation().setThreatBySafePawnEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.SLIDER_ON_QUEEN_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getSliderOnQueenMg(),
                (e, val) -> e.getThreatsEvaluation().setSliderOnQueenMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.SLIDER_ON_QUEEN_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getSliderOnQueenEg(),
                (e, val) -> e.getThreatsEvaluation().setSliderOnQueenEg(val),
                THREATS_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.KNIGHT_ON_QUEEN_MG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getKnightOnQueenMg(),
                (e, val) -> e.getThreatsEvaluation().setKnightOnQueenMg(val),
                THREATS_PARAMS_INTERVALl));
        groups.add(new IntegerValueParam(ParameterizedThreatsEvaluation.KNIGHT_ON_QUEEN_EG, parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getKnightOnQueenEg(),
                (e, val) -> e.getThreatsEvaluation().setKnightOnQueenEg(val),
                THREATS_PARAMS_INTERVALl));

    }

    private void addPassedPawnParameters(ParameterizedEvaluation parameterizedEvaluation) {
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_BLOCKED, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierBlocked(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierBlocked(val),
                PASSEDPAWN_FLOAT_INTERVALl));
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_NO_ENEMY_ATTACKS_IN_FRONT, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierNoEnemyAttacksInFront(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierNoEnemyAttacksInFront(val),
                PASSEDPAWN_FLOAT_INTERVALl));
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_NEXT_SQUARE_ATTACKED, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierNextSquareAttacked(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierNextSquareAttacked(val),
                PASSEDPAWN_FLOAT_INTERVALl));

        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_NEXT_SQARE_DEFENDED, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierNextSquareDefended(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierNextSquareDefended(val),
                PASSEDPAWN_FLOAT_INTERVALl));

        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_ENEMY_KING_IN_FRONT, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierEnemyKingInFront(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierEnemyKingInFront(val),
                PASSEDPAWN_FLOAT_INTERVALl));
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_ATTACKED, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierAttacked(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierAttacked(val),
                PASSEDPAWN_FLOAT_INTERVALl));
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_DEFENDED_BY_ROOK_FROM_BEHIND, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierDefendedByRookFromBehind(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierDefendedByRookFromBehind(val),
                PASSEDPAWN_FLOAT_INTERVALl));
        groups.add(new FloatValueParam(PassedPawnEval.MULTIPLIER_ATTACKED_BY_ROOK_FROM_BEHIND, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getMultiplierAttackedByRookFromBehind(),
                (e, val) -> e.getPawnEvaluation().getPassedPawnEval().setMultiplierAttackedByRookFromBehind(val),
                PASSEDPAWN_FLOAT_INTERVALl));

        groups.add(new ArrayFunctionParameterGroup(
                PassedPawnEval.PASSED_SCORE_EG,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getPassedScoreEg(),
                PASSEDPAWN_SCORE_INTERVALl));

        groups.add(new FloatArrayFunctionParameterGroup(
                PassedPawnEval.PASSED_KING_MULTI,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnEval().getPassedKingMulti(),
                PASSEDPAWN_FLOAT_INTERVALl));
    }

    private void addPawnParameters(ParameterizedEvaluation parameterizedEvaluation) {
        groups.add(new IntegerValueParam(PAWN_SHIELD_2, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getShield2(),
                (e, val) -> e.getPawnEvaluation().setShield2(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(PAWN_SHIELD_3, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getShield3(),
                (e, val) -> e.getPawnEvaluation().setShield3(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ATTACKED_PAWN_PENALTY, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getAttackedPawnPenalty(),
                (e, val) -> e.getPawnEvaluation()
                        .setAttackedPawnPenalty(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ISOLATED_PAWN_PENALTY, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getIsolatedPawnPenalty(),
                (e, val) -> parameterizedEvaluation.getPawnEvaluation()
                        .setIsolatedPawnPenalty(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(DOUBLE_PAWN_PENALTY, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getDoublePawnPenalty(),
                (e, val) -> e.getPawnEvaluation().setDoublePawnPenalty(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(BACKWARDED_PAWN_PENALTY, parameterizedEvaluation,
                e -> e.getPawnEvaluation().getBackwardedPawnPenalty(),
                (e, val) -> e.getPawnEvaluation()
                        .setBackwardedPawnPenalty(val),
                PAWN_PARAMS_INTERVALl));

        boolean mirrored = true;
        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, WEAK_PAWN_FILE, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getWeakPawnPst()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PROTECTED_PASSER_CSV, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getProtectedPasserPst()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PASSED_PAWN_FILE, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnPst()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, BLOCKED_PAWN_FILE, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getBlockedPawnPst()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PROTECTED_CSV, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getProtectedPst()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, NEIGHBOUR_CSV, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getNeighbourPst()));
    }

    private void addKingAttackParameters(ParameterizedEvaluation parameterizedEvaluation,
            ParameterizedMobilityEvaluation mobEval) {
        for (FigureType type : asList(Knight, Bishop, Rook, Queen, King)) {

            if (mobEval.getMobFigParams(type).kingAtt instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(mobEval.getMobFigParams(type).propertyKingAtt,
                        parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).kingAtt),
                        KINGATTACK_VALUE_INTERVAL));
            }
        }

    }

    private void addMobilityParameters(ParameterizedEvaluation parameterizedEvaluation,
            ParameterizedMobilityEvaluation mobEval) {
        for (FigureType type : asList(Knight, Bishop, Rook, Queen, King)) {

            MobFigParams figParams = mobEval.getMobFigParams(type);
            if (figParams.mobilityMG instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(figParams.propertyMobilityMg,
                        parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).mobilityMG),
                        MOBILITY_VALUE_INTERVAL));
            }
            if (figParams.mobilityEG instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(figParams.propertyMobilityEg,
                        parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).mobilityEG),
                        MOBILITY_VALUE_INTERVAL));
            }
        }
    }

    private void addPositionalParameters(ParameterizedEvaluation parameterizedEvaluation) {

        // additional positional parameters:
        IntIntervall individualParamsInterval = new IntIntervall(0, 100);

        groups.add(new IntegerValueParam(MobilityEvalResult.ROOK_HALF, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getRookHalf(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setRookHalf(val);
                    e.getMobEvaluation().getBResult().setRookHalf(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.ROOK_OPEN, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getRookOpen(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setRookOpen(val);
                    e.getMobEvaluation().getBResult().setRookOpen(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.EARLY_QUEEN_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getEarlyQueenPenalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setEarlyQueenPenalty(val);
                    e.getMobEvaluation().getBResult().setEarlyQueenPenalty(val);
                }, individualParamsInterval));

        //        groups.add(new IntegerValueParam(MobilityEvalResult.BISHOP_TRAPPED_A_6_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getBishopTrappedA6Penalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setBishopTrappedA6Penalty(val);
        //                    e.getMobEvaluation().getBResult().setBishopTrappedA6Penalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.BISHOP_TRAPPED_A_7_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getBishopTrappedA7Penalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setBishopTrappedA7Penalty(val);
        //                    e.getMobEvaluation().getBResult().setBishopTrappedA7Penalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.KING_BLOCKS_ROOK_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getKingBlocksRookPenalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setKingBlocksRookPenalty(val);
        //                    e.getMobEvaluation().getBResult().setKingBlocksRookPenalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.BLOCK_CENTRAL_PAWN_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getBlockCentralPawnPenalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setBlockCentralPawnPenalty(val);
        //                    e.getMobEvaluation().getBResult().setBlockCentralPawnPenalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.RETURNING_BISHOP, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getReturningBishop(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setReturningBishop(val);
        //                    e.getMobEvaluation().getBResult().setReturningBishop(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.C_3_KNIGHT_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getC3KnightPenalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setC3KnightPenalty(val);
        //                    e.getMobEvaluation().getBResult().setC3KnightPenalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.KNIGHT_TRAPPED_A_7_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getKnightTrappedA7Penalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setKnightTrappedA7Penalty(val);
        //                    e.getMobEvaluation().getBResult().setKnightTrappedA7Penalty(val);
        //                }, individualParamsInterval));
        //
        //        groups.add(new IntegerValueParam(MobilityEvalResult.KNIGHT_TRAPPED_A_8_PENALTY, parameterizedEvaluation,
        //                e -> e.getMobEvaluation().getWResult().getKnightTrappedA8Penalty(),
        //                (e, val) -> {
        //                    e.getMobEvaluation().getWResult().setKnightTrappedA8Penalty(val);
        //                    e.getMobEvaluation().getBResult().setKnightTrappedA8Penalty(val);
        //                }, individualParamsInterval));

    }

    private void addPstParameters(ParameterizedEvaluation parameterizedEvaluation) {
        boolean mirrored = true;
        groups.add(new PstPatternParameterGroup(PAWN_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getPawnMG));
        groups.add(new PstPatternParameterGroup(BISHOP_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getBishopMG));
        groups.add(new PstPatternParameterGroup(KNIGHT_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKnightMG));
        groups.add(new PstPatternParameterGroup(ROOK_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getRookMG));
        groups.add(new PstPatternParameterGroup(QUEEN_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getQueenMG));
        groups.add(new PstPatternParameterGroup(KING_MG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKingMG));

        groups.add(new PstPatternParameterGroup(PAWN_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getPawnEG));
        groups.add(new PstPatternParameterGroup(BISHOP_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getBishopEG));
        groups.add(new PstPatternParameterGroup(KNIGHT_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKnightEG));
        groups.add(new PstPatternParameterGroup(ROOK_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getRookEG));
        groups.add(new PstPatternParameterGroup(QUEEN_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getQueenEG));
        groups.add(new PstPatternParameterGroup(KING_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKingEG));
    }

    private void addMaterialParameters(ParameterizedEvaluation parameterizedEvaluation) {
        groups.add(new MaterialValueParam(MAT_PAWN_MG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getPawnMG, ParameterizedMaterialEvaluation::setPawnMG));
        groups.add(new MaterialValueParam(MAT_KNIGHT_MG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getKnightMG, ParameterizedMaterialEvaluation::setKnightMG));
        groups.add(new MaterialValueParam(MAT_BISHOP_MG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getBishopMG, ParameterizedMaterialEvaluation::setBishopMG));
        groups.add(new MaterialValueParam(MAT_ROOK_MG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getRookMG, ParameterizedMaterialEvaluation::setRookMG));
        groups.add(new MaterialValueParam(MAT_QUEEN_MG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getQueenMG, ParameterizedMaterialEvaluation::setQueenMG));

        groups.add(new MaterialValueParam(MAT_PAWN_EG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getPawnEG, ParameterizedMaterialEvaluation::setPawnEG));
        groups.add(new MaterialValueParam(MAT_KNIGHT_EG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getKnightEG, ParameterizedMaterialEvaluation::setKnightEG));
        groups.add(new MaterialValueParam(MAT_BISHOP_EG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getBishopEG, ParameterizedMaterialEvaluation::setBishopEG));
        groups.add(new MaterialValueParam(MAT_ROOK_EG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getRookEG, ParameterizedMaterialEvaluation::setRookEG));
        groups.add(new MaterialValueParam(MAT_QUEEN_EG, parameterizedEvaluation,
                ParameterizedMaterialEvaluation::getQueenEG, ParameterizedMaterialEvaluation::setQueenEG));
    }

    private void addAdjustmentParameters(ParameterizedEvaluation parameterizedEvaluation) {
        groups.add(new AdjustmentValueParam(TEMPO, parameterizedEvaluation,
                ParameterizedAdjustmentsEvaluation::getTempo, ParameterizedAdjustmentsEvaluation::setTempo));
        groups.add(new AdjustmentValueParam(BISHOP_PAIR, parameterizedEvaluation,
                ParameterizedAdjustmentsEvaluation::getBishopPair,
                ParameterizedAdjustmentsEvaluation::setBishopPair));
        groups.add(new AdjustmentValueParam(KNIGHT_PAIR, parameterizedEvaluation,
                ParameterizedAdjustmentsEvaluation::getKnightPair,
                ParameterizedAdjustmentsEvaluation::setKnightPair));
        groups.add(new AdjustmentValueParam(ROOK_PAIR, parameterizedEvaluation,
                ParameterizedAdjustmentsEvaluation::getRookPair, ParameterizedAdjustmentsEvaluation::setRookPair));
    }

    public String collectParamDescr() {
        // make them distinct (since some params may be related and return a description of the whole related parameters:
        Set<String> distinctSet = new LinkedHashSet<>();
        for (TuningParameterGroup group : groups) {
            distinctSet.add(group.getParamDef());
        }
        return distinctSet.stream()
                .collect(joining("\n"));
    }

    public void writeParamDescr(File outputDir) {
        outputDir.mkdirs();
        for (TuningParameterGroup group : groups) {
            group.writeParamDef(outputDir);
        }
    }

    public ParameterSet copy() {
        ParameterSet c = new ParameterSet();
        for (TuningParameterGroup group : groups) {
            c.groups.add(group.copy());
        }
        c.createRedundantParamsList();
        return c;
    }
}
