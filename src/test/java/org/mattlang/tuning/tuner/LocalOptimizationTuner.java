package org.mattlang.tuning.tuner;

import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.AppConfiguration.LOGGING_DIR;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizer;
import org.mattlang.tuning.TuneableEvaluateFunction;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizationTuner.class.getSimpleName());

    public static void main(String[] args) throws IOException {
        System.setProperty(LOGGING_ACTIVATE, "true");
        System.setProperty(LOGGING_DIR, ".");
        initLogging("/tuningLogging.properties");

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(args);
        dataset.setMultithreaded(true);
        LOGGER.info("Data set with " + dataset.getFens().size() + " Fens loaded.");

        LocalOptimizer optimizer = new LocalOptimizer();
        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction();

        LOGGER.info("Initial Parameter values:");
        LOGGER.info(evaluate.collectParamDescr());

        LOGGER.info("Opimizing...");
        List<TuningParameter> optimizedParams = optimizer.optimize(evaluate, dataset);

        LOGGER.info("Optimized Parameter values:");
        LOGGER.info(evaluate.collectParamDescr());
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

}
