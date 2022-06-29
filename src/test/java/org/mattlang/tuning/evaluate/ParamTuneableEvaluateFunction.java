package org.mattlang.tuning.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    private ParameterizedEvaluation parameterizedEvaluation = new ParameterizedEvaluation();

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
        params.add(new MaterialValueParam("pawnMg", matEval.getPawnMG(), v -> matEval.setPawnMG(v)));
        params.add(new MaterialValueParam("knightMg", matEval.getKnightMG(), v -> matEval.setKnightMG(v)));
        params.add(new MaterialValueParam("bishopMg", matEval.getBishopMG(), v -> matEval.setBishopMG(v)));
        params.add(new MaterialValueParam("rookMg", matEval.getRookMG(), v -> matEval.setRookMG(v)));
        params.add(new MaterialValueParam("queenMg", matEval.getQueenMG(), v -> matEval.setQueenMG(v)));

        return params;
    }

    @Override
    public void printResults() {

    }
}
