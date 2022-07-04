package org.mattlang.tuning.tuner;

import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizer;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizationTuner.class.getSimpleName());

    public static void main(String[] args) throws IOException {
        System.setProperty("jacky.logging.activate", "true");
        initLogging("/tuningLogging.properties");

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(args);
        dataset.setMultithreaded(true);
        LOGGER.info("Data set with " + dataset.getFens().size() + " Fens loaded.");

        LocalOptimizer optimizer = new LocalOptimizer();
        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction();

        LOGGER.info("Initial Parameter values:");
        LOGGER.info(collectParamDescr(evaluate.getParams()));

        LOGGER.info("Opimizing...");
        List<TuningParameter> optimizedParams = optimizer.optimize(evaluate, dataset);

        LOGGER.info("Optimized Parameter values:");
        LOGGER.info(collectParamDescr(optimizedParams));
    }

    private static DataSet loadDataset(String[] args) throws IOException {
        DatasetPreparer preparer = new DatasetPreparer();
        if (args != null && args.length > 0) {
            DataSet result = new DataSet();
            for (String arg : args) {
                LOGGER.info("parsing file " + arg);
                result.add(preparer.prepareLoadFromFile(new File(arg)));
            }
            return result;
        } else {

            InputStream in = LocalOptimizationTuner.class.getResourceAsStream("/testpgn/example.pgn");
            return preparer.prepareDatasetFromPgn(in);
        }
    }

    private static String collectParamDescr(List<TuningParameter> params) {
        // make them distinct (since some params may be related and return a description of the whole related parameters:
        Set<String> distinctSet = new LinkedHashSet<>();
        for (TuningParameter param : params) {
            distinctSet.add(param.getParamDef());
        }
        return distinctSet.stream()
                .collect(joining("\n"));
    }
}
