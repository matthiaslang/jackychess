package org.mattlang.tuning.tuner;

import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.AppConfiguration.LOGGING_DIR;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.tuning.*;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizationTuner.class.getSimpleName());

    /**
     * the paramset name to tune.
     */
    public static final String EVAL_PARAM_SET = "TUNED03";
    private final String[] args;

    private boolean multiThreading = true;

    private boolean adjustK = false;

    private boolean tuneMaterial = false;

    private boolean tunePst = true;

    private boolean removeDuplicateFens = true;

    private File outputDir = new File("./tuningoutput");

    public static final int THREAD_COUNT = 7;

    public static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    public LocalOptimizationTuner(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) throws IOException {

        LocalOptimizationTuner tuner = new LocalOptimizationTuner(args);
        tuner.run();

    }

    private void run() throws IOException {

        System.setProperty("opt.evalParamSet", EVAL_PARAM_SET);

        // set output dir to pst config dir:
        outputDir = new File("./src/main/resources/config/" + EVAL_PARAM_SET.toLowerCase() + "/pst/");

        System.setProperty(LOGGING_ACTIVATE, "true");
        System.setProperty(LOGGING_DIR, ".");
        initLogging("/tuningLogging.properties");

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(args);
        dataset.setMultithreaded(multiThreading);
        dataset.logInfos();
        if (removeDuplicateFens) {
            dataset.removeDuplidateFens();
            LOGGER.info("Statistics after removing duplicates:");
            dataset.logInfos();
        }

        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction(tuneMaterial, tunePst);

        if (adjustK) {
            LOGGER.info("Minimize Scaling K...");
            LocalOptimizerK optimizerK = new LocalOptimizerK();
            double k = optimizerK.optimize(evaluate, dataset);
            LOGGER.info("Scaling finished: K=" + k);
            dataset.setK(k);
        }

        LocalOptimizer optimizer = new LocalOptimizer(outputDir);

        LOGGER.info("Initial Parameter values:");
        LOGGER.info(evaluate.collectParamDescr());
        evaluate.writeParamDescr(outputDir);

        LOGGER.info("Opimizing...");
        List<TuningParameter> optimizedParams = optimizer.optimize(evaluate, dataset);

        LOGGER.info("Optimized Parameter values:");
        LOGGER.info(evaluate.collectParamDescr());

        executorService.shutdown();

    }

    private DataSet loadDataset(String[] args) throws IOException {
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
