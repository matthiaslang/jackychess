package org.mattlang.tuning.tuner;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizer;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    public static void main(String[] args) {

        LocalOptimizer optimizer=new LocalOptimizer();
        TuneableEvaluateFunction evaluate=new ParamTuneableEvaluateFunction();

        DataSet dataset= loadDataset();
        optimizer.optimize(evaluate, dataset);

        evaluate.printResults();

    }

    private static DataSet loadDataset() {
        return null;
    }

}
