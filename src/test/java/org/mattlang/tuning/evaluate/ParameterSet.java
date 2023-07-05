package org.mattlang.tuning.evaluate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.board.FigureType.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedAdjustmentsEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPawnEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation.*;
import static org.mattlang.tuning.evaluate.AdjustmentValueParam.ADJUSTMENT_VALUE_INTERVAL;

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
        if (optParams.isTuneAdjustmentsFactors()) {
            addAdjustmentParameterFactors(parameterizedEvaluation);
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
                THREATS_PARAMS_INTERVALl,
                e -> e.getThreatsEvaluation().updateCombinedArrays()));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_MINOR_EG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByMinorEg(),
                THREATS_PARAMS_INTERVALl,
                e -> e.getThreatsEvaluation().updateCombinedArrays()));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_ROOK_MG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByRookMg(),
                THREATS_PARAMS_INTERVALl,
                e -> e.getThreatsEvaluation().updateCombinedArrays()));

        groups.add(new ArrayFunctionParameterGroup(
                ParameterizedThreatsEvaluation.THREAT_BY_ROOK_EG,
                parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByRookEg(),
                THREATS_PARAMS_INTERVALl,
                e -> e.getThreatsEvaluation().updateCombinedArrays()));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByKingScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getHangingScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getWeakQueenProtectionScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getRestrictedPieceScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatByPawnPushScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getThreatBySafePawnScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getSliderOnQueenScore(),
                THREATS_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getThreatsEvaluation().getKnightOnQueenScore(),
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

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getPawnEvaluation().getAttackedPawnPenaltyScore(),
                PAWN_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getPawnEvaluation().getIsolatedPawnPenaltyScore(),
                PAWN_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getPawnEvaluation().getDoublePawnPenaltyScore(),
                PAWN_PARAMS_INTERVALl));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getPawnEvaluation().getBackwardedPawnPenaltyScore(),
                PAWN_PARAMS_INTERVALl));

        boolean mirrored = true;

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, WEAK_PAWN_FILE_MG, WEAK_PAWN_FILE_EG, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getWeakPawnPstMgEg()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PROTECTED_PASSER_CSV_MG, PROTECTED_PASSER_CSV_EG,
                mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getProtectedPasserPstMgEg()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PASSED_PAWN_FILE_MG, PASSED_PAWN_FILE_EG, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getPassedPawnPstMgEg()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, BLOCKED_PAWN_FILE_MG, BLOCKED_PAWN_FILE_EG, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getBlockedPawnPstMgEg()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, PROTECTED_CSV_MG, PROTECTED_CSV_EG, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getProtectedPstMgEg()));

        groups.add(new PatternParameterGroup(PAWN_CONFIG_SUB_DIR, NEIGHBOUR_CSV_MG, NEIGHBOUR_CSV_EG, mirrored,
                parameterizedEvaluation,
                e -> e.getPawnEvaluation().getNeighbourPstMgEg()));

    }

    private void addKingAttackParameters(ParameterizedEvaluation parameterizedEvaluation,
            ParameterizedMobilityEvaluation mobEval) {
        for (FigureType type : asList(Knight, Bishop, Rook, Queen, King)) {

            if (mobEval.getMobFigParams(type).kingAttMg instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(mobEval.getMobFigParams(type).propertyKingAttMg,
                        parameterizedEvaluation,
                        e -> (e.getMobEvaluation().getMobFigParams(type).kingAttMg),
                        KINGATTACK_VALUE_INTERVAL,
                        e -> e.getMobEvaluation().getMobFigParams(type).updateCombinedVals()));
            }
            if (mobEval.getMobFigParams(type).kingAttEg instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(mobEval.getMobFigParams(type).propertyKingAttEg,
                        parameterizedEvaluation,
                        e -> (e.getMobEvaluation().getMobFigParams(type).kingAttEg),
                        KINGATTACK_VALUE_INTERVAL,
                        e -> e.getMobEvaluation().getMobFigParams(type).updateCombinedVals()));
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
                        e -> e.getMobEvaluation().getMobFigParams(type).mobilityMG,
                        MOBILITY_VALUE_INTERVAL,
                        e -> e.getMobEvaluation().getMobFigParams(type).updateCombinedVals()));
            }
            if (figParams.mobilityEG instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(figParams.propertyMobilityEg,
                        parameterizedEvaluation,
                        e -> e.getMobEvaluation().getMobFigParams(type).mobilityEG,
                        MOBILITY_VALUE_INTERVAL,
                        e -> e.getMobEvaluation().getMobFigParams(type).updateCombinedVals()));
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

        groups.add(new PstPatternParameterGroup(PAWN_MG_CSV, PAWN_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getPawnMGEG));
        groups.add(new PstPatternParameterGroup(BISHOP_MG_CSV, BISHOP_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getBishopMGEG));
        groups.add(new PstPatternParameterGroup(KNIGHT_MG_CSV, KNIGHT_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKnightMGEG));
        groups.add(new PstPatternParameterGroup(ROOK_MG_CSV, ROOK_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getRookMGEG));
        groups.add(new PstPatternParameterGroup(QUEEN_MG_CSV, QUEEN_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getQueenMGEG));
        groups.add(new PstPatternParameterGroup(KING_MG_CSV, KING_EG_CSV, mirrored, parameterizedEvaluation,
                ParameterizedPstEvaluation::getKingMGEG));

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

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getAdjustments().getTempoScore(),
                ADJUSTMENT_VALUE_INTERVAL));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getAdjustments().getBishopPairScore(),
                ADJUSTMENT_VALUE_INTERVAL));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getAdjustments().getKnightPairScore(),
                ADJUSTMENT_VALUE_INTERVAL));

        groups.add(new ChangeableMgEgScoreParameterGroup(parameterizedEvaluation,
                e -> e.getAdjustments().getRookPairScore(),
                ADJUSTMENT_VALUE_INTERVAL));

    }

    private void addAdjustmentParameterFactors(ParameterizedEvaluation parameterizedEvaluation) {

        groups.add(new ArrayFunctionParameterGroup(KNIGHT_ADJ_MG,
                parameterizedEvaluation,
                e -> e.getAdjustments().getKnightAdjustmentMG(),
                MOBILITY_VALUE_INTERVAL,
                e -> e.getAdjustments().updateCombinedVals()));

        groups.add(new ArrayFunctionParameterGroup(KNIGHT_ADJ_EG,
                parameterizedEvaluation,
                e -> e.getAdjustments().getKnightAdjustmentEG(),
                MOBILITY_VALUE_INTERVAL,
                e -> e.getAdjustments().updateCombinedVals()));

        groups.add(new ArrayFunctionParameterGroup(ROOK_ADJ_MG,
                parameterizedEvaluation,
                e -> e.getAdjustments().getRookAdjustmentMG(),
                MOBILITY_VALUE_INTERVAL,
                e -> e.getAdjustments().updateCombinedVals()));

        groups.add(new ArrayFunctionParameterGroup(ROOK_ADJ_EG,
                parameterizedEvaluation,
                e -> e.getAdjustments().getRookAdjustmentEG(),
                MOBILITY_VALUE_INTERVAL,
                e -> e.getAdjustments().updateCombinedVals()));
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
