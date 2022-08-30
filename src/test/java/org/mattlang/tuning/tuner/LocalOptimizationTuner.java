package org.mattlang.tuning.tuner;

import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.AppConfiguration.LOGGING_DIR;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.tuning.*;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;

public class LocalOptimizationTuner {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizationTuner.class.getSimpleName());

    /**
     * Params, set to some standard params:
     */
    private OptParameters params = OptParameters.builder()
            .evalParamSet("TUNED001")
            .adjustK(false)
            .multiThreading(true)
            .threadCount(7)
            .removeDuplicateFens(true)
            .tunePst(true)
            .tuneMaterial(true)
            .build();
    private File outputDir;

    public static ExecutorService executorService = Executors.newFixedThreadPool(7);

    public LocalOptimizationTuner(String[] args) {
        this.params = OptParameters.builder().inputFiles(Arrays.asList(args)).build();
    }

    public LocalOptimizationTuner(OptParameters params) {
        this.params = params;

    }

    public static void main(String[] args) throws IOException {

        LocalOptimizationTuner tuner = new LocalOptimizationTuner(args);
        tuner.run();

    }

    public static void run(OptParameters params) throws IOException {
        LocalOptimizationTuner tuner = new LocalOptimizationTuner(params);
        executorService = Executors.newFixedThreadPool(params.getThreadCount());
        tuner.run();
    }

    private void run() throws IOException {

        System.setProperty("opt.evalParamSet", params.getEvalParamSet());

        // set output dir to pst config dir:
        outputDir = new File("./src/main/resources/config/" + params.getEvalParamSet().toLowerCase());

        System.setProperty(LOGGING_ACTIVATE, "true");
        System.setProperty(LOGGING_DIR, ".");
        initLogging("/tuningLogging.properties");

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(params.getInputFiles());
        dataset.setMultithreaded(params.isMultiThreading());
        dataset.logInfos();
        if (params.isRemoveDuplicateFens()) {
            dataset.removeDuplidateFens();
            LOGGER.info("Statistics after removing duplicates:");
            dataset.logInfos();
        }

        TuneableEvaluateFunction evaluate = new ParamTuneableEvaluateFunction(params.isTuneMaterial(), params.isTunePst());

        if (params.isAdjustK()) {
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
        evaluate.writeParamDescr(outputDir);

        executorService.shutdown();

    }

    private DataSet loadDataset(List<String> args) throws IOException {
        DatasetPreparer preparer = new DatasetPreparer(params);
        DataSet result = new DataSet(params);
        for (String arg : args) {
            LOGGER.info("parsing file " + arg);
            result.add(preparer.prepareLoadFromFile(new File(arg)));
        }
        return result;
    }

}
