package org.mattlang.tuning.evaluate;

import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation.*;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation.*;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    private ParameterizedEvaluation parameterizedEvaluation = new ParameterizedEvaluation(false);

    public ParamTuneableEvaluateFunction() {
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return parameterizedEvaluation.eval(currBoard, who2Move);
    }

    @Override
    public void setParams(List<TuningParameter> params) {
        for (TuningParameter param : params) {
            if (param instanceof MaterialValueParam) {
                ((MaterialValueParam) param).saveValue();
            }
        }
    }


    @Override
    public List<TuningParameter> getParams() {
        ArrayList<TuningParameter> params = new ArrayList<>();

        ParameterizedMaterialEvaluation matEval = parameterizedEvaluation.getMatEvaluation();
        params.add(new MaterialValueParam(MAT_PAWN_MG, matEval.getPawnMG(), v -> matEval.setPawnMG(v)));
        params.add(new MaterialValueParam(MAT_KNIGHT_MG, matEval.getKnightMG(), v -> matEval.setKnightMG(v)));
        params.add(new MaterialValueParam(MAT_BISHOP_MG, matEval.getBishopMG(), v -> matEval.setBishopMG(v)));
        params.add(new MaterialValueParam(MAT_ROOK_MG, matEval.getRookMG(), v -> matEval.setRookMG(v)));
        params.add(new MaterialValueParam(MAT_QUEEN_MG, matEval.getQueenMG(), v -> matEval.setQueenMG(v)));

        params.add(new MaterialValueParam(MAT_PAWN_EG, matEval.getPawnEG(), v -> matEval.setPawnEG(v)));
        params.add(new MaterialValueParam(MAT_KNIGHT_EG, matEval.getKnightEG(), v -> matEval.setKnightEG(v)));
        params.add(new MaterialValueParam(MAT_BISHOP_EG, matEval.getBishopEG(), v -> matEval.setBishopEG(v)));
        params.add(new MaterialValueParam(MAT_ROOK_EG, matEval.getRookEG(), v -> matEval.setRookEG(v)));
        params.add(new MaterialValueParam(MAT_QUEEN_EG, matEval.getQueenEG(), v -> matEval.setQueenEG(v)));

        ParameterizedPstEvaluation pstEval = parameterizedEvaluation.getPstEvaluation();
        for (int i = 0; i < 64; i++) {
            params.add(new PstValueParam(PAWN_MG_CSV, pstEval.getPawnMG(), i));
            params.add(new PstValueParam(BISHOP_MG_CSV, pstEval.getBishopMG(), i));
            params.add(new PstValueParam(KNIGHT_MG_CSV, pstEval.getKnightMG(), i));
            params.add(new PstValueParam(ROOK_MG_CSV, pstEval.getRookMG(), i));
            params.add(new PstValueParam(QUEEN_MG_CSV, pstEval.getQueenMG(), i));
            params.add(new PstValueParam(KING_MG_CSV, pstEval.getKingMG(), i));
        }

        return params;
    }

    @Override
    public TuneableEvaluateFunction copy() {
        // copy means so far just to create a new object.
        return new ParamTuneableEvaluateFunction();
    }

}
