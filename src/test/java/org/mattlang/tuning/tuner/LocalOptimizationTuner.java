package org.mattlang.tuning.tuner;

import java.io.File;
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

        System.out.println("Load & Prepare Data...");
        DataSet dataset = loadDataset(args);

        LocalOptimizer optimizer = new LocalOptimizer();
        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction();

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

    private static DataSet loadDataset(String[] args) throws IOException {
        DatasetPreparer preparer = new DatasetPreparer();
        if (args != null && args.length > 0) {
            DataSet result = new DataSet();
            for (String arg : args) {
                System.out.println("parsing file " + arg);
                result.add(preparer.prepareLoadFromFile(new File(arg)));
            }
            return result;
        } else {

            InputStream in = LocalOptimizationTuner.class.getResourceAsStream("/testpgn/example.pgn");
            return preparer.prepareDatasetFromPgn(in);
        }
    }

}
