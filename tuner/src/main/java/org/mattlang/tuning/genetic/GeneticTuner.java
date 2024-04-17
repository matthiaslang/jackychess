package org.mattlang.tuning.genetic;

import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.AppConfiguration.LOGGING_DIR;
import static org.mattlang.jc.Main.initLogging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.AbstractTuner;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.LocalOptimizerK;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.DatasetPreparer;
import org.mattlang.tuning.tuner.OptParameters;

public class GeneticTuner extends AbstractTuner {

    private static final Logger LOGGER = Logger.getLogger(GeneticTuner.class.getSimpleName());

    private File outputDir;

    public static ExecutorService executorService = Executors.newFixedThreadPool(7);

    public GeneticTuner(String[] args) {
        super(OptParameters.builder().inputFiles(Arrays.asList(args)).build());
    }

    public GeneticTuner(OptParameters params) {
        super(params);
    }

    public static void main(String[] args) throws IOException {

        GeneticTuner tuner = new GeneticTuner(args);
        tuner.run();

    }

    public static void run(OptParameters params) throws IOException {
        GeneticTuner tuner = new GeneticTuner(params);
        executorService = Executors.newFixedThreadPool(params.getThreadCount());
        tuner.run();
    }

    private void run() throws IOException {

        System.setProperty("opt.evalParamSet", params.getEvalParamSet());

        final String path = determineOutputPath();
        File filepath = new File(path);
        outputDir = new File(filepath, params.getEvalParamSet().toLowerCase());
        File mdFile = new File(outputDir, params.getName() + ".md");
        boolean continuingTuningRun = mdFile.exists();

        MarkdownAppender markdownAppender = new MarkdownAppender(mdFile);

        System.setProperty(LOGGING_ACTIVATE, "true");
        System.setProperty(LOGGING_DIR, ".");
        initLogging("/tuningLogging.properties");

        ParamTuneableEvaluateFunction evaluate =
                new ParamTuneableEvaluateFunction(params);
        copySourceConfigFile(outputDir);

        ParameterSet parameterSet = new ParameterSet(params, evaluate.getParameterizedEvaluation());

        // start evals for initial population:
        List<ParameterSet> startConfigs = new ArrayList<>();
        for (String startEvalConfig : params.getGeneticParams().getStartEvalConfigs()) {
            ParamTuneableEvaluateFunction startEval =
                    new ParamTuneableEvaluateFunction(startEvalConfig, params);
            startConfigs.add(new ParameterSet(params, startEval.getParameterizedEvaluation()));
        }

        // todo doesnt make sense in genetic tuning...
        if (!continuingTuningRun && params.isResetParametersBeforeTuning()) {
            LOGGER.info("Resetting Parameter values");
            for (TuningParameter param : parameterSet.getParams()) {
                param.resetValue();
            }
            parameterSet.writeParamDescr(outputDir);
        }

        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(params.getInputFiles());
        dataset.setMultithreaded(params.isMultiThreading());
        dataset.logInfos();
        if (params.isRemoveDuplicateFens()) {
            dataset.removeDuplidateFens();
            LOGGER.info("Statistics after removing duplicates:");
            dataset.logInfos();
        }
        // write or append the general infos for this run
        markdownAppender.append(w -> {
            params.writeMarkdownInfos(w);
            dataset.writeLogInfos(w);
        });

        if (params.isAdjustK()) {
            LOGGER.info("Minimize Scaling K...");
            LocalOptimizerK optimizerK = new LocalOptimizerK(params);
            double k = optimizerK.optimize(parameterSet, evaluate, dataset);
            LOGGER.info("Scaling finished: K=" + k);
            dataset.setK(k);

            markdownAppender.append(w -> {
                w.paragraph("K adjusted to: " + k);
            });
        } else {
            markdownAppender.append(w -> {
                w.paragraph("K: " + dataset.getK());
            });
        }

        SimpleGeneticAlgorithm optimizer =
                new SimpleGeneticAlgorithm(outputDir, params, evaluate, dataset, markdownAppender);

        LOGGER.info("Initial Parameter values:\n" + parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        LOGGER.info("Opimizing...");
        ParameterSet optimizedParams = optimizer.runAlgorithm(parameterSet, startConfigs);

        LOGGER.info("Optimized Parameter values:\n" + optimizedParams.collectParamDescr());
        optimizedParams.writeParamDescr(outputDir);

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
