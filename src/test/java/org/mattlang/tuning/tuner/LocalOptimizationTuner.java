package org.mattlang.tuning.tuner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizer;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    public static void main(String[] args) throws IOException {

        LocalOptimizer optimizer = new LocalOptimizer();
        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction();

        System.out.println("Load & Prepare Data...");
        DataSet dataset = loadDataset();

        System.out.println("Initial Parameter values:");
        for (TuningParameter param : evaluate.getParams()) {
            param.printResult();
        }

        System.out.println("Opimizing...");
        List<TuningParameter> optimizedParams = optimizer.optimize(evaluate, dataset);

        System.out.println("Optimized Parameter values:");
        for (TuningParameter param : optimizedParams) {
            param.printResult();
        }
    }

    private static DataSet loadDataset() throws IOException {
        DatasetPreparer preparer = new DatasetPreparer();
        InputStream in = LocalOptimizationTuner.class.getResourceAsStream("/testpgn/example.pgn");
        return preparer.prepareDatasetFromPgn(in);
    }

}
