package org.mattlang.tuning.evaluate;

import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    private ParameterizedEvaluation parameterizedEvaluation = ParameterizedEvaluation.createForTuning();

    ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    ArrayList<TuningParameter> params = new ArrayList<>();

    private boolean tuneMaterial;

    private boolean tunePst;

    public ParamTuneableEvaluateFunction(boolean tuneMaterial, boolean tunePst) {
        this.tuneMaterial = tuneMaterial;
        this.tunePst = tunePst;

        if (tuneMaterial) {
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

        if (tunePst) {
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

        for (TuningParameterGroup group : groups) {
            params.addAll(group.getParameters());
        }

    }

    private ParamTuneableEvaluateFunction(ArrayList<TuningParameterGroup> groups, ArrayList<TuningParameter> params) {
        this.groups = groups;
        this.params = params;
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
        return new ParamTuneableEvaluateFunction(this.groups, this.params);
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
