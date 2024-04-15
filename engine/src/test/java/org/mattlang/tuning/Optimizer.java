package org.mattlang.tuning;

import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;

public interface Optimizer {

    ParameterSet optimize(ParameterSet parameterSet, ParamTuneableEvaluateFunction evaluate, DataSet dataSet);
}
