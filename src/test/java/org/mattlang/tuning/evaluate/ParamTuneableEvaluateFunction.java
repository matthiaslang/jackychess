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
import java.util.List;
import java.util.Set;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.engine.evaluation.parameval.*;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobFigParams;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobilityEvalResult;
import org.mattlang.tuning.Intervall;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;
import org.mattlang.tuning.tuner.OptParameters;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    private static final Intervall MOBILITY_VALUE_INTERVAL = new Intervall(-500, +500);
    private static final Intervall KINGATTACK_VALUE_INTERVAL = new Intervall(0, +50);
    public static final Intervall PAWN_PARAMS_INTERVALl = new Intervall(0, 100);
    private ParameterizedEvaluation parameterizedEvaluation = ParameterizedEvaluation.createForTuning();

    ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    ArrayList<TuningParameter> params = new ArrayList<>();

    private final OptParameters optParams;

    public ParamTuneableEvaluateFunction(OptParameters optParams) {
        this.optParams = optParams;

        if (optParams.isTunePawnEval()) {
            addPawnParameters();
        }

        if (optParams.isTuneAdjustments()) {
            addAdjustmentParameters();
        }

        if (optParams.isTuneMaterial()) {
            addMaterialParameters();
        }

        if (optParams.isTunePst()) {
            addPstParameters();
        }
        ParameterizedMobilityEvaluation mobEval = parameterizedEvaluation.getMobEvaluation();

        if (optParams.isTuneMobility()) {
            addMobilityParameters(mobEval);
        }

        if (optParams.isTunePositional()) {
            addPositionalParameters(parameterizedEvaluation);
        }

        if (optParams.isTuneKingAttack()) {
            addKingAttackParameters(mobEval);
        }

        for (TuningParameterGroup group : groups) {
            params.addAll(group.getParameters());
        }

    }

    private void addPawnParameters() {
        groups.add(new IntegerValueParam(ParameterizedPawnEvaluation.PAWN_SHIELD_2, parameterizedEvaluation,
                parameterizedEvaluation -> parameterizedEvaluation.getPawnEvaluation().getShield2(),
                (parameterizedEvaluation, val) -> parameterizedEvaluation.getPawnEvaluation().setShield2(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedPawnEvaluation.PAWN_SHIELD_3, parameterizedEvaluation,
                parameterizedEvaluation -> parameterizedEvaluation.getPawnEvaluation().getShield3(),
                (parameterizedEvaluation, val) -> parameterizedEvaluation.getPawnEvaluation().setShield3(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedPawnEvaluation.ATTACKED_PAWN_PENALTY, parameterizedEvaluation,
                parameterizedEvaluation -> parameterizedEvaluation.getPawnEvaluation().getAttackedPawnPenalty(),
                (parameterizedEvaluation, val) -> parameterizedEvaluation.getPawnEvaluation()
                        .setAttackedPawnPenalty(val),
                PAWN_PARAMS_INTERVALl));

        groups.add(new IntegerValueParam(ParameterizedPawnEvaluation.DOUBLE_PAWN_PENALTY, parameterizedEvaluation,
                parameterizedEvaluation -> parameterizedEvaluation.getPawnEvaluation().getDoublePawnPenalty(),
                (parameterizedEvaluation, val) -> parameterizedEvaluation.getPawnEvaluation().setDoublePawnPenalty(val),
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

    }

    private void addKingAttackParameters(ParameterizedMobilityEvaluation mobEval) {
        for (FigureType type : asList(Knight, Bishop, Rook, Queen, King)) {

            if (mobEval.getMobFigParams(type).kingAtt instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(mobEval.getMobFigParams(type).propertyKingAtt,
                        parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).kingAtt),
                        KINGATTACK_VALUE_INTERVAL));
            }
        }

    }

    private void addMobilityParameters(ParameterizedMobilityEvaluation mobEval) {
        for (FigureType type : asList(Knight, Bishop, Rook, Queen, King)) {

            MobFigParams figParams = mobEval.getMobFigParams(type);
            if (figParams.mobilityMG instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(figParams.propertyMobilityMg,
                        this.parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).mobilityMG),
                        MOBILITY_VALUE_INTERVAL));
            }
            if (figParams.mobilityEG instanceof ArrayFunction) {
                groups.add(new ArrayFunctionParameterGroup(figParams.propertyMobilityEg,
                        this.parameterizedEvaluation,
                        e -> ((ArrayFunction) e.getMobEvaluation().getMobFigParams(type).mobilityEG),
                        MOBILITY_VALUE_INTERVAL));
            }
        }
    }

    private void addPositionalParameters(ParameterizedEvaluation parameterizedEvaluation) {

        // additional positional parameters:
        Intervall individualParamsInterval = new Intervall(0, 100);

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

        groups.add(new IntegerValueParam(MobilityEvalResult.BISHOP_TRAPPED_A_6_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getBishopTrappedA6Penalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setBishopTrappedA6Penalty(val);
                    e.getMobEvaluation().getBResult().setBishopTrappedA6Penalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.BISHOP_TRAPPED_A_7_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getBishopTrappedA7Penalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setBishopTrappedA7Penalty(val);
                    e.getMobEvaluation().getBResult().setBishopTrappedA7Penalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.KING_BLOCKS_ROOK_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getKingBlocksRookPenalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setKingBlocksRookPenalty(val);
                    e.getMobEvaluation().getBResult().setKingBlocksRookPenalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.BLOCK_CENTRAL_PAWN_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getBlockCentralPawnPenalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setBlockCentralPawnPenalty(val);
                    e.getMobEvaluation().getBResult().setBlockCentralPawnPenalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.RETURNING_BISHOP, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getReturningBishop(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setReturningBishop(val);
                    e.getMobEvaluation().getBResult().setReturningBishop(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.C_3_KNIGHT_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getC3KnightPenalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setC3KnightPenalty(val);
                    e.getMobEvaluation().getBResult().setC3KnightPenalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.KNIGHT_TRAPPED_A_7_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getKnightTrappedA7Penalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setKnightTrappedA7Penalty(val);
                    e.getMobEvaluation().getBResult().setKnightTrappedA7Penalty(val);
                }, individualParamsInterval));

        groups.add(new IntegerValueParam(MobilityEvalResult.KNIGHT_TRAPPED_A_8_PENALTY, parameterizedEvaluation,
                e -> e.getMobEvaluation().getWResult().getKnightTrappedA8Penalty(),
                (e, val) -> {
                    e.getMobEvaluation().getWResult().setKnightTrappedA8Penalty(val);
                    e.getMobEvaluation().getBResult().setKnightTrappedA8Penalty(val);
                }, individualParamsInterval));

    }

    private void addPstParameters() {
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

    private void addMaterialParameters() {
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

    private void addAdjustmentParameters() {
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

    private ParamTuneableEvaluateFunction(ArrayList<TuningParameterGroup> groups, ArrayList<TuningParameter> params,
            OptParameters optParams) {
        this.groups = groups;
        this.params = params;
        this.optParams = optParams;
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return parameterizedEvaluation.eval(currBoard, who2Move);
    }

    @Override
    public void saveValues(List<TuningParameter> params) {
        for (TuningParameter param : params) {
            param.saveValue(parameterizedEvaluation);
        }
    }


    @Override
    public List<TuningParameter> getParams() {
        return params;
    }

    @Override
    public TuneableEvaluateFunction copy() {
        // copy means so far just to create a new object.
        return new ParamTuneableEvaluateFunction(this.groups, this.params, this.optParams);
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

    @Override
    public void writeParamDescr(File outputDir) {
        outputDir.mkdirs();
        for (TuningParameterGroup group : groups) {
            group.writeParamDef(outputDir);
        }
    }
}
