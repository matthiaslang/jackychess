package org.mattlang.tuning.evaluate;

import java.io.File;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.TuningCache;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
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

    public ParamTuneableEvaluateFunction(OptParameters optParams, boolean continuingTuningRun) {
        this.optParams = optParams;

        if (continuingTuningRun && optParams.getOutputdir() != null) {
            EvalConfig evalConfig = new EvalConfig(new File(optParams.getOutputdir()));
            parameterizedEvaluation = ParameterizedEvaluation.createForTuning(evalConfig, optParams.isOptimizeMode());
        } else {
            parameterizedEvaluation =
                    ParameterizedEvaluation.createForTuning(new EvalConfig(), optParams.isOptimizeMode());
        }
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
    public TuningCache getTuningCache() {
        return parameterizedEvaluation.getTuningCache();
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
        return new ParamTuneableEvaluateFunction(this.optParams, false);
    }

}
