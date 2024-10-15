package org.mattlang.tuning.evaluate;

import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.search.SearchThreadContextCache;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;

public class ParamTuneableEvaluateFunction implements TuneableEvaluateFunction {

    @Getter
    private final ParameterizedEvaluation parameterizedEvaluation;

    private final OptParameters optParams;

    public ParamTuneableEvaluateFunction(OptParameters optParams) {
        this.optParams = optParams;
        parameterizedEvaluation = ParameterizedEvaluation.createForTuning();
    }

    public ParamTuneableEvaluateFunction(String startEvalConfig, OptParameters optParams) {
        this.optParams = optParams;
        parameterizedEvaluation = ParameterizedEvaluation.createForTuning(startEvalConfig);
    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        return parameterizedEvaluation.eval(currBoard, who2Move);
    }

    @Override
    public void associateThreadCache(SearchThreadContextCache cache) {
        // not used
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
