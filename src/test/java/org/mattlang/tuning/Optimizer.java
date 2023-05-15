package org.mattlang.tuning;

import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

import java.util.List;

public interface Optimizer {

    List<TuningParameter> optimize(ParamTuneableEvaluateFunction evaluate, DataSet dataSet);
}
