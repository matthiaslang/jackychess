package org.mattlang.tuning.evaluate;

import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.PawnCache;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    @Getter
    private ParameterizedEvaluation parameterizedEvaluation = ParameterizedEvaluation.createForTuning();

    private final OptParameters optParams;

    public ParamTuneableEvaluateFunction(OptParameters optParams) {
        this.optParams = optParams;
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return parameterizedEvaluation.eval(currBoard, who2Move);
    }

    @Override
    public int calcPstDelta(Color color, Move m) {
        return parameterizedEvaluation.calcPstDelta(color, m);
    }

    @Override
    public void setPawnCache(PawnCache pawnCache) {

    }

    @Override
    public void saveValues(List<TuningParameter> params) {
        for (TuningParameter param : params) {
            param.saveValue(parameterizedEvaluation);
        }
    }

    @Override
    public TuneableEvaluateFunction copy() {
        // copy means so far just to create a new object.
        return new ParamTuneableEvaluateFunction(this.optParams);
    }

}
