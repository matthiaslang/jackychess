package org.mattlang.tuning;

import java.util.List;

public interface Optimizer {

    List<TuningParameter> optimize(TuneableEvaluateFunction evaluate, DataSet dataSet);
}
